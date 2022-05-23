package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateScheduleEventDTO extends ScheduledEventDTO {

    private UpdateSpecificEventDTO updateSpecificEvent;
    private Boolean removeExceptionDates = false;

}
