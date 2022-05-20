package ec.com.jasr.geosatisschedule.modules.schedule.core.service;

import ec.com.jasr.geosatisschedule.core.application.AppSpringCtx;
import ec.com.jasr.geosatisschedule.core.util.AppException;
import ec.com.jasr.geosatisschedule.core.util.AppUtil;
import ec.com.jasr.geosatisschedule.modules.schedule.core.model.entity.Schedule;
import ec.com.jasr.geosatisschedule.modules.schedule.core.nativequery.ScheduleNativeQuery;
import ec.com.jasr.geosatisschedule.modules.schedule.core.repository.ScheduleRepository;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity.ScheduledEvent;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    public Schedule getScheduledEvent(Long scheduleId) throws AppException {
        Optional<Schedule> schedule = AppSpringCtx.getBean(ScheduleRepository.class).findById(scheduleId);

        if (schedule.isEmpty()) {
            AppUtil.throwError("Schedule with id: " + scheduleId + " not found", HttpStatus.NOT_FOUND);
        }

        return schedule.get();
    }

    @Transactional
    public Schedule saveNewSchedule(Schedule schedule) throws AppException {
        this.validateScheduleData(schedule);

        return AppSpringCtx.getBean(ScheduleRepository.class).save(schedule);
    }

    private void validateScheduleData(Schedule schedule) throws AppException {
        if (schedule.getName() == null) {
            AppUtil.throwError("Schedule must have a name", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean hasScheduledEvent(Long scheduleId, LocalDateTime date) {
        Boolean hasScheduledEvent = AppSpringCtx.getBean(ScheduleRepository.class).hasScheduledEvent(scheduleId, date, date.plusDays(1));
        return hasScheduledEvent;
    }
}