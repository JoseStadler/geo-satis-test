package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledEventRangeDateDTO {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
