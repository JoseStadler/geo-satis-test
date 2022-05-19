package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.service;

import ec.com.jasr.geosatisschedule.core.application.AppSpringCtx;
import ec.com.jasr.geosatisschedule.core.util.AppException;
import ec.com.jasr.geosatisschedule.core.util.AppUtil;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.dto.*;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity.RecurringScheduledEvent;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity.ScheduleEventException;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity.ScheduledEvent;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.repository.RecurringScheduledEventRepository;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.repository.ScheduleEventRepository;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util.ScheduleEventWeekDays;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.apache.commons.collections4.CollectionUtils;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleEventService {

    @Transactional
    public ScheduledEvent saveScheduleEvent(NewScheduledEventDTO dto) throws AppException {
        if (!this.isRecurrentEvent(dto) && !this.isRangeEvent(dto)) {
            AppUtil.throwError("Event must be defined using range or recurring option", HttpStatus.BAD_REQUEST);
        }

        if (this.isRecurrentEvent(dto)) {
            return this.saveNewRecurringEvent(dto);
        }
        return this.saveNewDateRangeEvent(dto);
    }

    private boolean isRecurrentEvent(ScheduledEventDTO dto) {
        return dto.getRecurring() != null;
    }

    private boolean isRangeEvent(ScheduledEventDTO dto) {
        return dto.getRange() != null;
    }

    @Transactional
    public ScheduledEvent saveNewDateRangeEvent(NewScheduledEventDTO dto) throws AppException {
        ScheduledEvent event = new ScheduledEvent();
        this.validateAndSetScheduledEventData(dto, event);
        return AppSpringCtx.getBean(ScheduleEventRepository.class).save(event);
    }

    @Transactional
    public RecurringScheduledEvent saveNewRecurringEvent(NewScheduledEventDTO dto) throws AppException {
        RecurringScheduledEvent event = new RecurringScheduledEvent();
        ScheduledEventRecurringDataDTO recurringDTO = dto.getRecurring();
        this.validateDataForRecurringScheduledEvent(recurringDTO);

        this.setRecurringEventData(event, recurringDTO);
        this.prepareDateRangeForScheduleEvent(dto, recurringDTO);
        this.setScheduledEventData(dto, event);

        return AppSpringCtx.getBean(RecurringScheduledEventRepository.class).save(event);
    }

    private void setRecurringEventData(RecurringScheduledEvent recurringEvent, ScheduledEventRecurringDataDTO recurringDTO) {
        recurringEvent.setDay(recurringDTO.getDay());
        recurringEvent.setInterval(recurringDTO.getInterval());
        recurringEvent.setIntervalType(recurringDTO.getIntervalType());

        if (CollectionUtils.isNotEmpty(recurringDTO.getExceptionDates())) {
            recurringEvent.setExceptions(this.prepareExceptionDates(recurringEvent, recurringDTO));
        }
    }

    private List<ScheduleEventException> prepareExceptionDates(RecurringScheduledEvent recurringEvent, ScheduledEventRecurringDataDTO recurringDTO) {
        return recurringDTO.getExceptionDates().stream()
                .map(exceptionDate -> {
                    ScheduleEventException exception = new ScheduleEventException();
                    exception.setExceptionDate(exceptionDate);
                    exception.setScheduledEvent(recurringEvent);
                    return exception;
                }).collect(Collectors.toList());
    }

    private void validateDataForRecurringScheduledEvent(ScheduledEventRecurringDataDTO recurringDto) throws AppException {
        if (recurringDto.getDay() == null) {
            AppUtil.throwError("Recurring event must have day option set up", HttpStatus.BAD_REQUEST);
        }

        if (recurringDto.getStartTime() == null) {
            AppUtil.throwError("Recurring event must have a start time", HttpStatus.BAD_REQUEST);
        }

        if (recurringDto.getEndTime() == null) {
            AppUtil.throwError("Recurring event must have a end time", HttpStatus.BAD_REQUEST);
        }

        if (recurringDto.getInterval() != null && recurringDto.getIntervalType() == null) {
            AppUtil.throwError("Recurring event with interval must have the interval type set up", HttpStatus.BAD_REQUEST);
        }

        if (recurringDto.getIntervalType() != null && recurringDto.getInterval() == null) {
            AppUtil.throwError("Recurring event with interval type must have the interval set up", HttpStatus.BAD_REQUEST);
        }
    }

    private void prepareDateRangeForScheduleEvent(ScheduledEventDTO dto, ScheduledEventRecurringDataDTO recurringDTO) {
        LocalDate eventDate = getLocalDateByRecurringDay(recurringDTO.getDay());
        this.setRangeDateForScheduledEvent(dto, eventDate, recurringDTO);
    }

    private LocalDate getLocalDateByRecurringDay(ScheduleEventWeekDays day) {
        switch (day) {
            case EVERY_DAY:
                return LocalDate.now();
            case WEEK_DAY:
                return AppUtil.getNextWeekDate();
            case WEEKENDS:
                return AppUtil.getNextWeekendDate();
            default:
                return AppUtil.getNextDateOf(DayOfWeek.of(day.getValue()));
        }
    }

    private void setRangeDateForScheduledEvent(ScheduledEventDTO dto, LocalDate eventDate, ScheduledEventRecurringDataDTO recurringDTO) {
        dto.setRange(new ScheduledEventRangeDateDTO());
        dto.getRange().setStartDate(eventDate.atTime(recurringDTO.getStartTime()).atOffset(ZoneOffset.UTC).toLocalDateTime());
        dto.getRange().setEndDate(eventDate.atTime(recurringDTO.getEndTime()).atOffset(ZoneOffset.UTC).toLocalDateTime());
    }

    private void validateAndSetScheduledEventData(NewScheduledEventDTO dto, ScheduledEvent event) throws AppException {
        this.validateDatesForScheduledEvent(dto);
        this.setScheduledEventData(dto, event);
    }

    private void validateDatesForScheduledEvent(NewScheduledEventDTO dto) throws AppException {
        this.validateScheduleId(dto.getScheduleId());
        this.validateDatesForScheduledEvent(dto.getRange().getStartDate(), dto.getRange().getEndDate());
    }

    private void validateScheduleId(Long scheduleId) throws AppException {
        if (scheduleId == null) {
            AppUtil.throwError("Schedule event must have a scheduleId", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateDatesForScheduledEvent(LocalDateTime startDate, LocalDateTime endDate) throws AppException {
        if (startDate == null) {
            AppUtil.throwError("Schedule event must have a startDate", HttpStatus.BAD_REQUEST);
        }
        if (endDate == null) {
            AppUtil.throwError("Schedule event must have a endDate", HttpStatus.BAD_REQUEST);
        }
        if (endDate.atOffset(ZoneOffset.UTC).isBefore(startDate.atOffset(ZoneOffset.UTC))) {
            AppUtil.throwError("End date has to be after start date", HttpStatus.BAD_REQUEST);
        }
    }

    private void setScheduledEventData(NewScheduledEventDTO dto, ScheduledEvent event) {
        event.setScheduleId(dto.getScheduleId());
        event.setStartDate(dto.getRange().getStartDate().atOffset(ZoneOffset.UTC).toLocalDateTime());
        event.setEndDate(dto.getRange().getEndDate().atOffset(ZoneOffset.UTC).toLocalDateTime());
    }

    @Transactional
    public ScheduledEvent updateScheduledEvent(Long id, UpdateScheduleEventDTO updateDataDTO) throws AppException {
        this.validateUpdateData(updateDataDTO);

        if (this.isUpdateSpecificEvent(updateDataDTO)) {
            return this.updateSpecificRecurringEvent(id, updateDataDTO.getUpdateSpecificEvent());
        }
        if (this.isRecurrentEvent(updateDataDTO)) {
            return this.updateDateRecurringEvent(id, updateDataDTO);
        }
        return this.updateDateRangeEvent(id, updateDataDTO);
    }

    private void validateUpdateData(UpdateScheduleEventDTO updateDataDTO) throws AppException {
        if (!this.isRecurrentEvent(updateDataDTO) && !this.isRangeEvent(updateDataDTO) && ! this.isUpdateSpecificEvent(updateDataDTO)) {
            AppUtil.throwError("Event must be updated using range, recurring or updateSpecificEvent option", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isUpdateSpecificEvent(UpdateScheduleEventDTO updateDataDTO) {
        return updateDataDTO.getUpdateSpecificEvent() != null;
    }

    private ScheduledEvent updateSpecificRecurringEvent(Long id, UpdateSpecificEventDTO specificEventDTO) throws AppException {
        RecurringScheduledEvent savedEvent = this.findSavedRecurringEvent(id);
        this.setDateAsExceptionInRecurringEvent(savedEvent, specificEventDTO);
        return this.saveSpecificNewEvent(savedEvent, specificEventDTO);
    }

    private ScheduledEvent setDateAsExceptionInRecurringEvent(RecurringScheduledEvent savedEvent, UpdateSpecificEventDTO specificEventDTO) throws AppException {
        UpdateScheduleEventDTO updateScheduleEventDTO = new UpdateScheduleEventDTO();
        ScheduledEventRecurringDataDTO recurringDataDTO =  new ScheduledEventRecurringDataDTO();
        List<LocalDate> exceptionDates = new ArrayList<>();

        exceptionDates.add(specificEventDTO.getDate());
        recurringDataDTO.setExceptionDates(exceptionDates);
        updateScheduleEventDTO.setRecurring(recurringDataDTO);

        return this.updateAndSaveRecurringEvent(savedEvent, updateScheduleEventDTO);
    }

    private ScheduledEvent saveSpecificNewEvent(RecurringScheduledEvent savedEvent, UpdateSpecificEventDTO specificEventDTO) throws AppException {
        NewScheduledEventDTO newScheduledEventDTO = new NewScheduledEventDTO();
        ScheduledEventRangeDateDTO range = new ScheduledEventRangeDateDTO();

        range.setStartDate(specificEventDTO.getDate().atTime(specificEventDTO.getStartTime()));
        range.setEndDate(specificEventDTO.getDate().atTime(specificEventDTO.getEndTime()));
        newScheduledEventDTO.setScheduleId(savedEvent.getScheduleId());
        newScheduledEventDTO.setRange(range);

        return this.saveNewDateRangeEvent(newScheduledEventDTO);
    }

    private ScheduledEvent updateDateRecurringEvent(Long id, UpdateScheduleEventDTO updateDataDTO) throws AppException {
        RecurringScheduledEvent savedEvent = this.findSavedRecurringEvent(id);

        if (this.isPastEvent(savedEvent)) {
            return this.saveScheduleEvent(this.initNewScheduleEvent(savedEvent, updateDataDTO));
        }

        return this.updateAndSaveRecurringEvent(savedEvent, updateDataDTO);
    }

    private RecurringScheduledEvent findSavedRecurringEvent(Long id) throws AppException {
        Optional<RecurringScheduledEvent> optionalScheduledEvent = AppSpringCtx.getBean(RecurringScheduledEventRepository.class).findById(id);
        if (optionalScheduledEvent.isEmpty()) {
            AppUtil.throwError("Event with id: " + id + " could not be found", HttpStatus.NOT_FOUND);
        }

        return optionalScheduledEvent.get();
    }

    private NewScheduledEventDTO initNewScheduleEvent(RecurringScheduledEvent savedEvent, UpdateScheduleEventDTO updateDataDTO) {
        NewScheduledEventDTO newEvent = new NewScheduledEventDTO();
        ScheduledEventRecurringDataDTO recurringData = new ScheduledEventRecurringDataDTO();

        recurringData.setDay(updateDataDTO.getRecurring().getDay() != null ? updateDataDTO.getRecurring().getDay() : savedEvent.getDay());
        recurringData.setInterval(updateDataDTO.getRecurring().getInterval() != null ? updateDataDTO.getRecurring().getInterval() : savedEvent.getInterval());
        recurringData.setIntervalType(updateDataDTO.getRecurring().getIntervalType() != null ? updateDataDTO.getRecurring().getIntervalType() : savedEvent.getIntervalType());
        recurringData.setStartTime(updateDataDTO.getRecurring().getStartTime() != null ? updateDataDTO.getRecurring().getStartTime() : savedEvent.getStartDate().toLocalTime());
        recurringData.setEndTime(updateDataDTO.getRecurring().getEndTime() != null ? updateDataDTO.getRecurring().getEndTime() : savedEvent.getEndDate().toLocalTime());
        recurringData.setExceptionDates(CollectionUtils.isNotEmpty(updateDataDTO.getRecurring().getExceptionDates()) ? updateDataDTO.getRecurring().getExceptionDates() : savedEvent.getExceptions().stream().map(ScheduleEventException::getExceptionDate).collect(Collectors.toList()));

        newEvent.setScheduleId(savedEvent.getScheduleId());
        newEvent.setRecurring(recurringData);

        return newEvent;
    }

    private void updateRecurringDateValues(RecurringScheduledEvent savedEvent, UpdateScheduleEventDTO updateDataDTO) {
        ScheduledEventRecurringDataDTO recurringDTO = updateDataDTO.getRecurring();

        if (recurringDTO.getDay() != null) {
            savedEvent.setDay(recurringDTO.getDay());
            this.updateEventLocalDate(savedEvent, recurringDTO);
        } else {
            this.updateEventDates(savedEvent, recurringDTO, savedEvent.getStartDate().toLocalDate());
        }
        if (recurringDTO.getInterval() != null) {
            savedEvent.setInterval(recurringDTO.getInterval());
        }
        if (recurringDTO.getIntervalType() != null) {
            savedEvent.setIntervalType(recurringDTO.getIntervalType());
        }
        if (updateDataDTO.getRemoveExceptionDates()) {
            savedEvent.getExceptions().clear();
        }
        if (CollectionUtils.isNotEmpty(recurringDTO.getExceptionDates())) {
            savedEvent.getExceptions().addAll(this.prepareExceptionDates(savedEvent, recurringDTO));
        }
    }

    private void updateEventLocalDate(RecurringScheduledEvent savedEvent, ScheduledEventRecurringDataDTO recurringDTO) {
        LocalDate eventDate = getLocalDateByRecurringDay(recurringDTO.getDay());
        this.updateEventDates(savedEvent, recurringDTO, eventDate);
    }

    private void updateEventDates(RecurringScheduledEvent savedEvent, ScheduledEventRecurringDataDTO recurringDTO, LocalDate eventDate) {
        savedEvent.setStartDate(eventDate.atTime(recurringDTO.getStartTime() != null ? recurringDTO.getStartTime() : savedEvent.getStartDate().toLocalTime()));
        savedEvent.setEndDate(eventDate.atTime(recurringDTO.getEndTime() != null ? recurringDTO.getEndTime() : savedEvent.getEndDate().toLocalTime()));
    }

    private ScheduledEvent updateAndSaveRecurringEvent(RecurringScheduledEvent savedEvent, UpdateScheduleEventDTO updateDataDTO) throws AppException {
        this.updateRecurringDateValues(savedEvent, updateDataDTO);
        return this.saveUpdatedRecurringDate(savedEvent);
    }

    private ScheduledEvent saveUpdatedRecurringDate(RecurringScheduledEvent savedEvent) throws AppException {
        this.validateDatesForScheduledEvent(savedEvent.getStartDate(), savedEvent.getEndDate());
        return AppSpringCtx.getBean(ScheduleEventRepository.class).save(savedEvent);
    }

    private ScheduledEvent updateDateRangeEvent(Long id, UpdateScheduleEventDTO updateDataDTO) throws AppException {
        ScheduledEvent savedEvent = this.findSavedRangeEvent(id);
        this.validateIfEventCanBeUpdated(savedEvent);
        this.updateDateRangeValues(savedEvent, updateDataDTO);
        this.validateDatesForScheduledEvent(savedEvent.getStartDate(), savedEvent.getEndDate());
        return AppSpringCtx.getBean(ScheduleEventRepository.class).save(savedEvent);
    }

    private void validateIfEventCanBeUpdated(ScheduledEvent savedEvent) throws AppException {
        if (this.isPastEvent(savedEvent)) {
            AppUtil.throwError("Past schedules can not be updated", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isPastEvent(ScheduledEvent savedEvent) {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).isAfter(savedEvent.getStartDate().atOffset(ZoneOffset.UTC));
    }

    private ScheduledEvent findSavedRangeEvent(Long id) throws AppException {
        Optional<ScheduledEvent> optionalScheduledEvent = AppSpringCtx.getBean(ScheduleEventRepository.class).findById(id);
        if (optionalScheduledEvent.isEmpty()) {
            AppUtil.throwError("Event with id: " + id + " could not be found", HttpStatus.NOT_FOUND);
        }

        return optionalScheduledEvent.get();
    }

    private void updateDateRangeValues(ScheduledEvent savedEvent, UpdateScheduleEventDTO updateDataDTO) {
        if (updateDataDTO.getRange().getStartDate() != null) {
            savedEvent.setStartDate(updateDataDTO.getRange().getStartDate().atOffset(ZoneOffset.UTC).toLocalDateTime());
        }

        if (updateDataDTO.getRange().getEndDate() != null) {
            savedEvent.setEndDate(updateDataDTO.getRange().getEndDate().atOffset(ZoneOffset.UTC).toLocalDateTime());
        }
    }

}
