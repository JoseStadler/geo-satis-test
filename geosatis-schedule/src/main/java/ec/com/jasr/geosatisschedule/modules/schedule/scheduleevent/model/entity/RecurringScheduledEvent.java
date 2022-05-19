package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util.ScheduleEventWeekDays;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.util.ScheduledEventIntervalType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "schedule_event", foreignKey = @ForeignKey(name = "recu_sche_even_schedule_event_sche_even_fk"))
@Table(name = "recurring_schedule_event", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"schedule_event"}, name = "recu_sche_even_schedule_event_uk")})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecurringScheduledEvent extends ScheduledEvent {

    @Enumerated(EnumType.STRING)
    @Column
    private ScheduleEventWeekDays day;

    @Column
    private Integer interval;

    @Enumerated(EnumType.STRING)
    @Column(name = "interval_type")
    private ScheduledEventIntervalType intervalType;

    @JsonManagedReference("RecurringScheduledEventScheduleEventExceptions")
    @OneToMany(mappedBy = "scheduledEvent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEventException> exceptions = new ArrayList<>();

    public ScheduleEventWeekDays getDay() {
        return day;
    }

    public void setDay(ScheduleEventWeekDays day) {
        this.day = day;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public ScheduledEventIntervalType getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(ScheduledEventIntervalType intervalType) {
        this.intervalType = intervalType;
    }

    public List<ScheduleEventException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ScheduleEventException> exceptions) {
        this.exceptions = exceptions;
    }
}
