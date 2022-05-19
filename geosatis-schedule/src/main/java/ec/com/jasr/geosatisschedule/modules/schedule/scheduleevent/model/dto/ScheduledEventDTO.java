package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.dto;

import lombok.Data;

@Data
public class ScheduledEventDTO {

    private ScheduledEventRangeDateDTO range;
    private ScheduledEventRecurringDataDTO recurring;

}
