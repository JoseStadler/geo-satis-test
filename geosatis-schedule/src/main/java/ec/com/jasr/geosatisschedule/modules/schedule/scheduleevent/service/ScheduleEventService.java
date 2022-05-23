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
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util.ScheduledEventConstants;
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
    public ScheduledEvent saveScheduleEvent(NewScheduledEventDTO newEventData) throws AppException {
        this.validateNewScheduledEventData(newEventData);

        if (this.isRecurrentEvent(newEventData)) {
            return this.saveNewRecurringEvent(newEventData);
        }
        return this.saveNewDateRangeEvent(newEventData);
    }

    private void validateNewScheduledEventData(NewScheduledEventDTO newEventData) throws AppException {
        if (!this.isRecurrentEvent(newEventData) && !this.isRangeEvent(newEventData)) {
            AppUtil.throwError(ScheduledEventConstants.EVENT_MUST_BE_DEFINED_USING_RANGE_OR_RECURRING_OPTION, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isRecurrentEvent(ScheduledEventDTO eventData) {
        return eventData.getRecurring() != null;
    }

    private boolean isRangeEvent(ScheduledEventDTO eventData) {
        return eventData.getRange() != null;
    }

    @Transactional
    public ScheduledEvent saveNewDateRangeEvent(NewScheduledEventDTO newEventData) throws AppException {
        ScheduledEvent event = new ScheduledEvent();
        this.validateAndSetScheduledEventData(newEventData, event);
        return AppSpringCtx.getBean(ScheduleEventRepository.class).save(event);
    }

    @Transactional
    public RecurringScheduledEvent saveNewRecurringEvent(NewScheduledEventDTO newEventData) throws AppException {
        RecurringScheduledEvent event = new RecurringScheduledEvent();
        ScheduledEventRecurringDataDTO recurringDTO = newEventData.getRecurring();
        this.validateDataForRecurringScheduledEvent(recurringDTO);

        this.setRecurringEventData(event, recurringDTO);
        this.prepareDateRangeForScheduleEvent(newEventData, recurringDTO);
        this.setScheduledEventData(newEventData, event);

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
            AppUtil.throwError(ScheduledEventConstants.RECURRING_EVENT_MUST_HAVE_DAY_OPTION_SET_UP, HttpStatus.BAD_REQUEST);
        }

        if (recurringDto.getStartTime() == null) {
            AppUtil.throwError(ScheduledEventConstants.RECURRING_EVENT_MUST_HAVE_A_START_TIME, HttpStatus.BAD_REQUEST);
        }

        if (recurringDto.getEndTime() == null) {
            AppUtil.throwError(ScheduledEventConstants.RECURRING_EVENT_MUST_HAVE_A_END_TIME, HttpStatus.BAD_REQUEST);
        }

        if (recurringDto.getInterval() != null && recurringDto.getIntervalType() == null) {
            AppUtil.throwError(ScheduledEventConstants.RECURRING_EVENT_WITH_INTERVAL_MUST_HAVE_THE_INTERVAL_TYPE_SET_UP, HttpStatus.BAD_REQUEST);
        }

        if (recurringDto.getIntervalType() != null && recurringDto.getInterval() == null) {
            AppUtil.throwError(ScheduledEventConstants.RECURRING_EVENT_WITH_INTERVAL_TYPE_MUST_HAVE_THE_INTERVAL_SET_UP, HttpStatus.BAD_REQUEST);
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

    private void setRangeDateForScheduledEvent(ScheduledEventDTO eventDTO, LocalDate eventDate, ScheduledEventRecurringDataDTO recurringDTO) {
        eventDTO.setRange(new ScheduledEventRangeDateDTO());
        eventDTO.getRange().setStartDate(eventDate.atTime(recurringDTO.getStartTime()).atOffset(ZoneOffset.UTC).toLocalDateTime());
        eventDTO.getRange().setEndDate(eventDate.atTime(recurringDTO.getEndTime()).atOffset(ZoneOffset.UTC).toLocalDateTime());
    }

    private void validateAndSetScheduledEventData(NewScheduledEventDTO newEventData, ScheduledEvent event) throws AppException {
        this.validateDatesForScheduledEvent(newEventData);
        this.setScheduledEventData(newEventData, event);
    }

    private void validateDatesForScheduledEvent(NewScheduledEventDTO newEventData) throws AppException {
        this.validateScheduleId(newEventData.getScheduleId());
        this.validateDatesForScheduledEvent(newEventData.getRange().getStartDate(), newEventData.getRange().getEndDate());
    }

    private void validateScheduleId(Long scheduleId) throws AppException {
        if (scheduleId == null) {
            AppUtil.throwError(ScheduledEventConstants.SCHEDULE_EVENT_MUST_HAVE_A_SCHEDULE_ID, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateDatesForScheduledEvent(LocalDateTime startDate, LocalDateTime endDate) throws AppException {
        if (startDate == null) {
            AppUtil.throwError(ScheduledEventConstants.SCHEDULE_EVENT_MUST_HAVE_A_START_DATE, HttpStatus.BAD_REQUEST);
        }
        if (endDate == null) {
            AppUtil.throwError(ScheduledEventConstants.SCHEDULE_EVENT_MUST_HAVE_A_END_DATE, HttpStatus.BAD_REQUEST);
        }
        if (endDate.atOffset(ZoneOffset.UTC).isBefore(startDate.atOffset(ZoneOffset.UTC))) {
            AppUtil.throwError(ScheduledEventConstants.END_DATE_HAS_TO_BE_AFTER_START_DATE, HttpStatus.BAD_REQUEST);
        }
    }

    private void setScheduledEventData(NewScheduledEventDTO newEventData, ScheduledEvent event) {
        event.setScheduleId(newEventData.getScheduleId());
        event.setStartDate(newEventData.getRange().getStartDate().atOffset(ZoneOffset.UTC).toLocalDateTime());
        event.setEndDate(newEventData.getRange().getEndDate().atOffset(ZoneOffset.UTC).toLocalDateTime());
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
            AppUtil.throwError(ScheduledEventConstants.EVENT_MUST_BE_UPDATED_USING_RANGE_RECURRING_OR_UPDATE_SPECIFIC_EVENT_OPTION, HttpStatus.BAD_REQUEST);
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

    private RecurringScheduledEvent setDateAsExceptionInRecurringEvent(RecurringScheduledEvent savedEvent, UpdateSpecificEventDTO specificEventDTO) throws AppException {
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

    @Transactional
    private ScheduledEvent updateDateRecurringEvent(Long id, UpdateScheduleEventDTO updateDataDTO) throws AppException {
        RecurringScheduledEvent savedEvent = this.findSavedRecurringEvent(id);

        if (this.isPastEvent(savedEvent)) {
            this.stopPreviousRecurringEvent(savedEvent);
            return this.saveScheduleEvent(this.initNewScheduleEvent(savedEvent, updateDataDTO));
        }

        return this.updateAndSaveRecurringEvent(savedEvent, updateDataDTO);
    }

    private void stopPreviousRecurringEvent(RecurringScheduledEvent savedEvent) {
        savedEvent.setStopRecurringDate(LocalDate.now());
        AppSpringCtx.getBean(RecurringScheduledEventRepository.class).save(savedEvent);
    }

    private RecurringScheduledEvent findSavedRecurringEvent(Long id) throws AppException {
        Optional<RecurringScheduledEvent> optionalScheduledEvent = AppSpringCtx.getBean(RecurringScheduledEventRepository.class).findById(id);
        if (!optionalScheduledEvent.isPresent()) {
            AppUtil.throwError(ScheduledEventConstants.EVENT_WITH_ID + id + ScheduledEventConstants.COULD_NOT_BE_FOUND, HttpStatus.NOT_FOUND);
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

    private RecurringScheduledEvent updateAndSaveRecurringEvent(RecurringScheduledEvent savedEvent, UpdateScheduleEventDTO updateDataDTO) throws AppException {
        this.updateRecurringDateValues(savedEvent, updateDataDTO);
        return this.saveUpdatedRecurringDate(savedEvent);
    }

    private RecurringScheduledEvent saveUpdatedRecurringDate(RecurringScheduledEvent savedEvent) throws AppException {
        this.validateDatesForScheduledEvent(savedEvent.getStartDate(), savedEvent.getEndDate());
        return AppSpringCtx.getBean(RecurringScheduledEventRepository.class).save(savedEvent);
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
            AppUtil.throwError(ScheduledEventConstants.PAST_SCHEDULES_CAN_NOT_BE_UPDATED, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isPastEvent(ScheduledEvent savedEvent) {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).isAfter(savedEvent.getStartDate().atOffset(ZoneOffset.UTC));
    }

    private ScheduledEvent findSavedRangeEvent(Long id) throws AppException {
        Optional<ScheduledEvent> optionalScheduledEvent = AppSpringCtx.getBean(ScheduleEventRepository.class).findById(id);
        if (!optionalScheduledEvent.isPresent()) {
            AppUtil.throwError(ScheduledEventConstants.EVENT_WITH_ID + id + ScheduledEventConstants.COULD_NOT_BE_FOUND, HttpStatus.NOT_FOUND);
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
