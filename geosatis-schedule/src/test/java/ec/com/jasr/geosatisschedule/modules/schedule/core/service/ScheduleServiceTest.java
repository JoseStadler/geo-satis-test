package ec.com.jasr.geosatisschedule.modules.schedule.core.service;

import ec.com.jasr.geosatisschedule.core.util.AppException;
import ec.com.jasr.geosatisschedule.modules.schedule.core.model.entity.Schedule;
import ec.com.jasr.geosatisschedule.modules.schedule.core.repository.ScheduleRepository;
import ec.com.jasr.geosatisschedule.modules.schedule.core.util.ScheduleConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class ScheduleServiceTest {

    private ScheduleService scheduleService;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    void setUp() {
        scheduleService = new ScheduleService();
    }

    @Test
    void shouldRetrieveTestSchedule() {
        Schedule schedule = new Schedule();

        Schedule retrieved = null;
        schedule.setId(1L);
        schedule.setName("Test");
        Optional<Schedule> scheduleOptional = Optional.of(schedule);
        given(this.scheduleRepository.findById(any())).willReturn(scheduleOptional);

        try {
            retrieved = scheduleService.getScheduledEvent(1L);
        } catch (AppException e) {
            e.printStackTrace();
        }
        assertEquals("Test", retrieved.getName());
    }
    @Test
    void shouldFailRetrievingTestSchedule() {
        Long scheduleId = 0L;
        Optional<Schedule> scheduleOptional = Optional.empty();
        given(this.scheduleRepository.findById(any())).willReturn(scheduleOptional);
        try {
            scheduleService.getScheduledEvent(scheduleId);
        } catch (AppException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getCode());
            assertEquals(ScheduleConstants.SCHEDULE_WITH_ID + scheduleId  + ScheduleConstants.NOT_FOUND, e.getMessage());
        }
    }

    @Test
    void shouldSaveNewSchedule() {
        Schedule schedule = new Schedule();
        Schedule saved = new Schedule();

        schedule.setName("Test");
        saved.setName(schedule.getName());
        saved.setId(1L);

        given(this.scheduleRepository.save(any())).willReturn(saved);
        try {
            Schedule retrieved = scheduleService.saveNewSchedule(schedule);
            assertEquals("Test", retrieved.getName());
            assertEquals(1L, retrieved.getId());
        } catch (AppException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldFailAsTheNameIsNotSetUp() {
        Schedule schedule = new Schedule();
        Schedule saved = new Schedule();

        saved.setName(schedule.getName());
        saved.setId(1L);

        given(this.scheduleRepository.save(any())).willReturn(saved);
        try {
            Schedule retrieved = scheduleService.saveNewSchedule(schedule);
        } catch (AppException e) {
            assertEquals(ScheduleConstants.SCHEDULE_MUST_HAVE_A_NAME, e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getCode());
        }
    }

    @Test
    void hasScheduledEvent() {
        given(this.scheduleRepository.hasScheduledEvent(any(), any(), any())).willReturn(true);
        assertEquals(true, scheduleService.hasScheduledEvent(1L, LocalDateTime.now()));
    }
}