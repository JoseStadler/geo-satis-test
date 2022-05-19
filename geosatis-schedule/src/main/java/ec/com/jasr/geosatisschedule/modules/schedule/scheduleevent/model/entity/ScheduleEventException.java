package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import ec.com.jasr.geosatisschedule.core.model.entity.BaseId;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "schedule_event_exception")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleEventException extends BaseId {

    @JsonBackReference("RecurringScheduledEventScheduleEventExceptions")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "schedule_event", foreignKey = @ForeignKey(name = "sche_even_exce_schedule_event_recu_sche_fk"))
    private RecurringScheduledEvent scheduledEvent;

    @Column(name = "exception_date", nullable = false)
    private LocalDate exceptionDate;

    public RecurringScheduledEvent getScheduledEvent() {
        return scheduledEvent;
    }

    public void setScheduledEvent(RecurringScheduledEvent scheduledEvent) {
        this.scheduledEvent = scheduledEvent;
    }

    public LocalDate getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(LocalDate exceptionDate) {
        this.exceptionDate = exceptionDate;
    }
}
