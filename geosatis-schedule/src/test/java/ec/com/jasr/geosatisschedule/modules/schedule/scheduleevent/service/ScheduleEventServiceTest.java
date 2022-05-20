package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.service;

import ec.com.jasr.geosatisschedule.core.util.AppException;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.dto.*;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity.RecurringScheduledEvent;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity.ScheduledEvent;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.repository.RecurringScheduledEventRepository;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.repository.ScheduleEventRepository;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util.ScheduleEventWeekDays;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util.ScheduledEventConstants;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util.ScheduledEventIntervalType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class ScheduleEventServiceTest {

    private ScheduleEventService scheduleEventService;
    private NewScheduledEventDTO newEventData;
    private UpdateScheduleEventDTO updateDataDTO;

    @MockBean
    private RecurringScheduledEventRepository recurringScheduledEventRepository;
    @MockBean
    private ScheduleEventRepository scheduleEventRepository;

    @BeforeEach
    void setUp() {
        scheduleEventService = new ScheduleEventService();
        newEventData = new NewScheduledEventDTO();
        newEventData.setScheduleId(1L);
        updateDataDTO = new UpdateScheduleEventDTO();
    }

    @AfterEach
    void tearDown() {
        newEventData = new NewScheduledEventDTO();
        newEventData.setScheduleId(1L);
        updateDataDTO = new UpdateScheduleEventDTO();
    }

    @Test
    void shouldThrowErrorAsNewScheduledEventDTONotProperlySet() {
        try {
            scheduleEventService.saveScheduleEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.EVENT_MUST_BE_DEFINED_USING_RANGE_OR_RECURRING_OPTION, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldSaveNewDateRangeEvent() {
        ScheduledEvent saved = null;
        ScheduledEventRangeDateDTO scheduledEventRangeDateDTO = new ScheduledEventRangeDateDTO();
        scheduledEventRangeDateDTO.setStartDate(LocalDateTime.now());
        scheduledEventRangeDateDTO.setEndDate(LocalDateTime.now().plusHours(2));
        newEventData.setRange(scheduledEventRangeDateDTO);
        ScheduledEvent event = new ScheduledEvent();
        event.setId(1L);
        given(this.scheduleEventRepository.save(any())).willReturn(event);
        try {
            saved = scheduleEventService.saveNewDateRangeEvent(newEventData);
        } catch (AppException e) {
        }
        assertEquals(1L, saved.getId());
    }

    @Test
    void shouldFailSaveNewDateRangeEventAsScheduleIdIsNotSet() {
        given(this.scheduleEventRepository.save(any())).willReturn(null);
        ScheduledEventRangeDateDTO scheduledEventRangeDateDTO = new ScheduledEventRangeDateDTO();
        scheduledEventRangeDateDTO.setStartDate(LocalDateTime.now());
        scheduledEventRangeDateDTO.setEndDate(LocalDateTime.now().plusHours(2));
        newEventData.setRange(scheduledEventRangeDateDTO);
        newEventData.setScheduleId(null);
        try {
            scheduleEventService.saveNewDateRangeEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.SCHEDULE_EVENT_MUST_HAVE_A_SCHEDULE_ID, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldFailSaveNewDateRangeEventAsStartDateIsNotSet() {
        given(this.scheduleEventRepository.save(any())).willReturn(null);
        ScheduledEventRangeDateDTO scheduledEventRangeDateDTO = new ScheduledEventRangeDateDTO();
        scheduledEventRangeDateDTO.setEndDate(LocalDateTime.now().plusHours(2));
        newEventData.setRange(scheduledEventRangeDateDTO);
        try {
            scheduleEventService.saveNewDateRangeEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.SCHEDULE_EVENT_MUST_HAVE_A_START_DATE, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldFailSaveNewDateRangeEventAsEndDateIsNotSet() {
        given(this.scheduleEventRepository.save(any())).willReturn(null);
        ScheduledEventRangeDateDTO scheduledEventRangeDateDTO = new ScheduledEventRangeDateDTO();
        scheduledEventRangeDateDTO.setStartDate(LocalDateTime.now());
        newEventData.setRange(scheduledEventRangeDateDTO);
        try {
            scheduleEventService.saveNewDateRangeEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.SCHEDULE_EVENT_MUST_HAVE_A_END_DATE, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldFailSaveNewDateRangeEventAsEndDateIsBeforeStartDate() {
        given(this.scheduleEventRepository.save(any())).willReturn(null);
        ScheduledEventRangeDateDTO scheduledEventRangeDateDTO = new ScheduledEventRangeDateDTO();
        scheduledEventRangeDateDTO.setStartDate(LocalDateTime.now());
        scheduledEventRangeDateDTO.setEndDate(LocalDateTime.now().plusHours(-2));
        newEventData.setRange(scheduledEventRangeDateDTO);
        try {
            scheduleEventService.saveNewDateRangeEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.END_DATE_HAS_TO_BE_AFTER_START_DATE, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void saveNewRecurringEvent() {
        RecurringScheduledEvent saved = null;
        ScheduledEventRecurringDataDTO scheduledEventRecurringDataDTO = new ScheduledEventRecurringDataDTO();
        scheduledEventRecurringDataDTO.setDay(ScheduleEventWeekDays.MONDAY);
        scheduledEventRecurringDataDTO.setStartTime(LocalTime.now());
        scheduledEventRecurringDataDTO.setEndTime(LocalTime.now().plus(Duration.ofHours(2)));
        newEventData.setRecurring(scheduledEventRecurringDataDTO);
        RecurringScheduledEvent event = new RecurringScheduledEvent();
        event.setId(1L);
        given(this.recurringScheduledEventRepository.save(any())).willReturn(event);
        try {
            saved = scheduleEventService.saveNewRecurringEvent(newEventData);
        } catch (AppException e) {
        }
        assertEquals(1L, saved.getId());
    }

    @Test
    void shouldFailOnSaveNewRecurringEventDayIsNotSet() {
        ScheduledEventRecurringDataDTO scheduledEventRecurringDataDTO = new ScheduledEventRecurringDataDTO();
        newEventData.setRecurring(scheduledEventRecurringDataDTO);
        try {
            scheduleEventService.saveScheduleEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.RECURRING_EVENT_MUST_HAVE_DAY_OPTION_SET_UP, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldFailOnSaveNewRecurringEventStartTimeIsNotSet() {
        ScheduledEventRecurringDataDTO scheduledEventRecurringDataDTO = new ScheduledEventRecurringDataDTO();
        scheduledEventRecurringDataDTO.setDay(ScheduleEventWeekDays.MONDAY);
        newEventData.setRecurring(scheduledEventRecurringDataDTO);
        try {
            scheduleEventService.saveScheduleEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.RECURRING_EVENT_MUST_HAVE_A_START_TIME, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldFailOnSaveNewRecurringEventEndTimeIsNotSet() {
        ScheduledEventRecurringDataDTO scheduledEventRecurringDataDTO = new ScheduledEventRecurringDataDTO();
        scheduledEventRecurringDataDTO.setDay(ScheduleEventWeekDays.MONDAY);
        scheduledEventRecurringDataDTO.setStartTime(LocalTime.now());
        newEventData.setRecurring(scheduledEventRecurringDataDTO);
        try {
            scheduleEventService.saveScheduleEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.RECURRING_EVENT_MUST_HAVE_A_END_TIME, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldFailOnSaveNewRecurringEventIntervalTypeIsNotSet() {
        ScheduledEventRecurringDataDTO scheduledEventRecurringDataDTO = new ScheduledEventRecurringDataDTO();
        scheduledEventRecurringDataDTO.setDay(ScheduleEventWeekDays.MONDAY);
        scheduledEventRecurringDataDTO.setStartTime(LocalTime.now());
        scheduledEventRecurringDataDTO.setEndTime(LocalTime.now().plus(Duration.ofHours(2)));
        scheduledEventRecurringDataDTO.setInterval(1);
        newEventData.setRecurring(scheduledEventRecurringDataDTO);
        try {
            scheduleEventService.saveScheduleEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.RECURRING_EVENT_WITH_INTERVAL_MUST_HAVE_THE_INTERVAL_TYPE_SET_UP, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldFailOnSaveNewRecurringEventIntervalIsNotSet() {
        ScheduledEventRecurringDataDTO scheduledEventRecurringDataDTO = new ScheduledEventRecurringDataDTO();
        scheduledEventRecurringDataDTO.setDay(ScheduleEventWeekDays.MONDAY);
        scheduledEventRecurringDataDTO.setStartTime(LocalTime.now());
        scheduledEventRecurringDataDTO.setEndTime(LocalTime.now().plus(Duration.ofHours(2)));
        scheduledEventRecurringDataDTO.setIntervalType(ScheduledEventIntervalType.DAY);
        newEventData.setRecurring(scheduledEventRecurringDataDTO);
        try {
            scheduleEventService.saveScheduleEvent(newEventData);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.RECURRING_EVENT_WITH_INTERVAL_TYPE_MUST_HAVE_THE_INTERVAL_SET_UP, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldFailUpdateScheduledEventAsUpdateDataIsNotProperlySend() {
        try {
            scheduleEventService.updateScheduledEvent(1l, updateDataDTO);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.EVENT_MUST_BE_UPDATED_USING_RANGE_RECURRING_OR_UPDATE_SPECIFIC_EVENT_OPTION, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldSetExceptionDateToSavedRecurringEvent() {
        LocalDate today = LocalDate.now();
        ScheduledEvent savedEvent = new ScheduledEvent();

        UpdateSpecificEventDTO updateSpecificEventDTO = new UpdateSpecificEventDTO();
        updateSpecificEventDTO.setDate(today);
        updateSpecificEventDTO.setStartTime(LocalTime.now());
        updateSpecificEventDTO.setEndTime(LocalTime.now().plusHours(2));
        updateDataDTO.setUpdateSpecificEvent(updateSpecificEventDTO);

        RecurringScheduledEvent savedRecurringScheduledEvent = new RecurringScheduledEvent();
        savedRecurringScheduledEvent.setScheduleId(1L);
        savedRecurringScheduledEvent.setId(1L);
        savedRecurringScheduledEvent.setStartDate(LocalDateTime.now());
        savedRecurringScheduledEvent.setDay(ScheduleEventWeekDays.MONDAY);
        savedRecurringScheduledEvent.setEndDate(LocalDateTime.now());
        given(this.recurringScheduledEventRepository.findById(any())).willReturn(Optional.of(savedRecurringScheduledEvent));
        given(this.recurringScheduledEventRepository.save(savedRecurringScheduledEvent)).willReturn(savedRecurringScheduledEvent);
        given(this.scheduleEventRepository.save(any())).willReturn(savedEvent);

        try {
            scheduleEventService.updateScheduledEvent(1l, updateDataDTO);
        } catch (AppException e) {
        }
        assertEquals(true, savedRecurringScheduledEvent.getExceptions().stream().anyMatch(ex -> ex.getExceptionDate().equals(today)));
    }

    @Test
    void shouldUpdateRecurringEventDateToNewDay() {
        LocalDate today = LocalDate.now();
        ScheduledEvent savedEvent = new ScheduledEvent();

        ScheduledEventRecurringDataDTO scheduledEventRecurringDataDTO = new ScheduledEventRecurringDataDTO();
        scheduledEventRecurringDataDTO.setDay(ScheduleEventWeekDays.TUESDAY);
        updateDataDTO.setRecurring(scheduledEventRecurringDataDTO);

        RecurringScheduledEvent savedRecurringScheduledEvent = new RecurringScheduledEvent();
        savedRecurringScheduledEvent.setScheduleId(1L);
        savedRecurringScheduledEvent.setId(1L);
        savedRecurringScheduledEvent.setStartDate(LocalDateTime.now().plusHours(6));
        savedRecurringScheduledEvent.setDay(ScheduleEventWeekDays.MONDAY);
        savedRecurringScheduledEvent.setEndDate(LocalDateTime.now().plusHours(7));
        given(this.recurringScheduledEventRepository.findById(any())).willReturn(Optional.of(savedRecurringScheduledEvent));
        given(this.recurringScheduledEventRepository.save(savedRecurringScheduledEvent)).willReturn(savedRecurringScheduledEvent);

        try {
            scheduleEventService.updateScheduledEvent(1l, updateDataDTO);
        } catch (AppException e) {
        }
        assertNotEquals(today, savedRecurringScheduledEvent.getStartDate().toLocalDate());
    }

    @Test
    void shouldSetStopDateToSavedRecurringEvent() {
        LocalDate today = LocalDate.now();
        ScheduledEvent savedEvent = new ScheduledEvent();

        ScheduledEventRecurringDataDTO scheduledEventRecurringDataDTO = new ScheduledEventRecurringDataDTO();
        scheduledEventRecurringDataDTO.setDay(ScheduleEventWeekDays.TUESDAY);
        updateDataDTO.setRecurring(scheduledEventRecurringDataDTO);

        RecurringScheduledEvent savedRecurringScheduledEvent = new RecurringScheduledEvent();
        savedRecurringScheduledEvent.setScheduleId(1L);
        savedRecurringScheduledEvent.setId(1L);
        savedRecurringScheduledEvent.setStartDate(LocalDateTime.now());
        savedRecurringScheduledEvent.setDay(ScheduleEventWeekDays.MONDAY);
        savedRecurringScheduledEvent.setEndDate(LocalDateTime.now());
        given(this.recurringScheduledEventRepository.findById(any())).willReturn(Optional.of(savedRecurringScheduledEvent));
        given(this.recurringScheduledEventRepository.save(savedRecurringScheduledEvent)).willReturn(savedRecurringScheduledEvent);
        given(this.scheduleEventRepository.save(any())).willReturn(savedEvent);

        try {
            scheduleEventService.updateScheduledEvent(1l, updateDataDTO);
        } catch (AppException e) {
        }
        assertEquals(today, savedRecurringScheduledEvent.getStopRecurringDate());
    }

    @Test
    void shouldNotUpdatePastSavedRangeEvent() {
        LocalDateTime today = LocalDateTime.now();
        ScheduledEvent savedEvent = new ScheduledEvent();
        ScheduledEventRangeDateDTO scheduledEventRangeDateDTO = new ScheduledEventRangeDateDTO();
        scheduledEventRangeDateDTO.setEndDate(today.plusDays(1));
        updateDataDTO.setRange(scheduledEventRangeDateDTO);

        ScheduledEvent savedScheduledEvent = new ScheduledEvent();
        savedScheduledEvent.setId(1L);
        savedScheduledEvent.setScheduleId(1L);
        savedScheduledEvent.setStartDate(today);
        savedScheduledEvent.setEndDate(today.plusHours(1));
        given(this.scheduleEventRepository.findById(any())).willReturn(Optional.of(savedScheduledEvent));

        try {
            scheduleEventService.updateScheduledEvent(1l, updateDataDTO);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.PAST_SCHEDULES_CAN_NOT_BE_UPDATED, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldFailOnUpdateSavedRangeEventWithEndDateBeforeStartDate() {
        LocalDateTime today = LocalDateTime.now();
        ScheduledEvent savedEvent = new ScheduledEvent();
        ScheduledEventRangeDateDTO scheduledEventRangeDateDTO = new ScheduledEventRangeDateDTO();
        scheduledEventRangeDateDTO.setEndDate(today.plusDays(-1));
        updateDataDTO.setRange(scheduledEventRangeDateDTO);

        ScheduledEvent savedScheduledEvent = new ScheduledEvent();
        savedScheduledEvent.setId(1L);
        savedScheduledEvent.setScheduleId(1L);
        savedScheduledEvent.setStartDate(today.plusHours(1));
        savedScheduledEvent.setEndDate(today.plusHours(2));
        given(this.scheduleEventRepository.findById(any())).willReturn(Optional.of(savedScheduledEvent));
        given(this.scheduleEventRepository.save(any())).willReturn(new ScheduledEvent());

        try {
            scheduleEventService.updateScheduledEvent(1l, updateDataDTO);
        } catch (AppException e) {
            assertEquals(ScheduledEventConstants.END_DATE_HAS_TO_BE_AFTER_START_DATE, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void shouldUpdateSavedRangeEvent() {
        LocalDateTime today = LocalDateTime.now();
        ScheduledEvent savedEvent = new ScheduledEvent();
        ScheduledEventRangeDateDTO scheduledEventRangeDateDTO = new ScheduledEventRangeDateDTO();
        scheduledEventRangeDateDTO.setEndDate(today.plusDays(1));
        updateDataDTO.setRange(scheduledEventRangeDateDTO);

        ScheduledEvent savedScheduledEvent = new ScheduledEvent();
        savedScheduledEvent.setId(1L);
        savedScheduledEvent.setScheduleId(1L);
        savedScheduledEvent.setStartDate(today.plusHours(1));
        savedScheduledEvent.setEndDate(today.plusHours(2));
        given(this.scheduleEventRepository.findById(any())).willReturn(Optional.of(savedScheduledEvent));
        given(this.scheduleEventRepository.save(any())).willReturn(new ScheduledEvent());

        try {
            scheduleEventService.updateScheduledEvent(1l, updateDataDTO);
        } catch (AppException e) {
        }
        assertEquals(scheduledEventRangeDateDTO.getEndDate(), savedScheduledEvent.getEndDate());
    }
}