package ec.com.jasr.geosatisschedule.modules.schedule.core.controller;

import ec.com.jasr.geosatisschedule.core.application.AppSpringCtx;
import ec.com.jasr.geosatisschedule.core.util.BaseController;
import ec.com.jasr.geosatisschedule.modules.schedule.core.model.entity.Schedule;
import ec.com.jasr.geosatisschedule.modules.schedule.core.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/schedules")
public class ScheduleController extends BaseController {

    @GetMapping(value = "{scheduleId}")
    public ResponseEntity<Object> getScheduledEvent(@PathVariable Long scheduleId) {
        try {
            return new ResponseEntity<>(AppSpringCtx.getBean(ScheduleService.class).getScheduledEvent(scheduleId), HttpStatus.OK);
        } catch (Exception ex) {
            return this.exceptionControllerManagement(ex);
        }
    }

    @PutMapping
    public ResponseEntity<Object> saveNewSchedule(@RequestBody Schedule schedule) {
        try {
            return new ResponseEntity<>(AppSpringCtx.getBean(ScheduleService.class).saveNewSchedule(schedule), HttpStatus.OK);
        } catch (Exception ex) {
            return this.exceptionControllerManagement(ex);
        }
    }
}
