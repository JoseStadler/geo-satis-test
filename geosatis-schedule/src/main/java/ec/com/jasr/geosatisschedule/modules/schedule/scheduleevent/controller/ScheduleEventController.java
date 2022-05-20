package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.controller;

import ec.com.jasr.geosatisschedule.core.application.AppSpringCtx;
import ec.com.jasr.geosatisschedule.core.util.BaseController;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.dto.NewScheduledEventDTO;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.dto.UpdateScheduleEventDTO;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.service.ScheduleEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/schedules/events")
public class ScheduleEventController extends BaseController {

    @PutMapping
    public ResponseEntity<Object> saveNewScheduledEvent(@RequestBody NewScheduledEventDTO dto) {
        try {
            return new ResponseEntity<>(AppSpringCtx.getBean(ScheduleEventService.class).saveScheduleEvent(dto), HttpStatus.OK);
        } catch (Exception ex) {
            return this.exceptionControllerManagement(ex);
        }
    }

    @PostMapping(value = "{id}")
    public ResponseEntity<Object> updateScheduledEvent(@PathVariable Long id, @RequestBody UpdateScheduleEventDTO dto) {
        try {
            return new ResponseEntity<>(AppSpringCtx.getBean(ScheduleEventService.class).updateScheduledEvent(id, dto), HttpStatus.OK);
        } catch (Exception ex) {
            return this.exceptionControllerManagement(ex);
        }
    }

}
