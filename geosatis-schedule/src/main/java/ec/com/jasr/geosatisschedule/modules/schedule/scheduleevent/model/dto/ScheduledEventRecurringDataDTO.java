package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.dto;

import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util.ScheduleEventWeekDays;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util.ScheduledEventIntervalType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduledEventRecurringDataDTO {

    private LocalTime startTime;
    private LocalTime endTime;
    private ScheduleEventWeekDays day;
    private Integer interval;
    private ScheduledEventIntervalType intervalType;
    private List<LocalDate> exceptionDates = new ArrayList<>();

}
