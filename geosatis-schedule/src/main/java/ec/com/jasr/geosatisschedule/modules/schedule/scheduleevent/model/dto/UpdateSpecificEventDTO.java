package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UpdateSpecificEventDTO {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

}
