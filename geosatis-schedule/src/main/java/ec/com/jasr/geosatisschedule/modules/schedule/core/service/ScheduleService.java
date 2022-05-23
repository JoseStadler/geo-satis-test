package ec.com.jasr.geosatisschedule.modules.schedule.core.service;

import ec.com.jasr.geosatisschedule.core.application.AppSpringCtx;
import ec.com.jasr.geosatisschedule.core.util.AppException;
import ec.com.jasr.geosatisschedule.core.util.AppUtil;
import ec.com.jasr.geosatisschedule.modules.schedule.core.model.entity.Schedule;
import ec.com.jasr.geosatisschedule.modules.schedule.core.repository.ScheduleRepository;
import ec.com.jasr.geosatisschedule.modules.schedule.core.util.ScheduleConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ScheduleService {

    public Schedule getScheduledEvent(Long scheduleId) throws AppException {
        Optional<Schedule> schedule = AppSpringCtx.getBean(ScheduleRepository.class).findById(scheduleId);

        if (!schedule.isPresent()) {
            AppUtil.throwError(ScheduleConstants.SCHEDULE_WITH_ID + scheduleId + ScheduleConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
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
            AppUtil.throwError(ScheduleConstants.SCHEDULE_MUST_HAVE_A_NAME, HttpStatus.BAD_REQUEST);
        }
    }

    public boolean hasScheduledEvent(Long scheduleId, LocalDateTime date) {
        return AppSpringCtx.getBean(ScheduleRepository.class).hasScheduledEvent(scheduleId, date, date.plusDays(1));
    }
}
