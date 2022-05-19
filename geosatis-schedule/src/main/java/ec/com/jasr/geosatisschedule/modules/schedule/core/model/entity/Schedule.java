package ec.com.jasr.geosatisschedule.modules.schedule.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ec.com.jasr.geosatisschedule.core.model.entity.BaseId;
import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity.ScheduledEvent;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "sche_name_uk")})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Schedule extends BaseId {

    @Column(nullable = false)
    private String name;

    @JsonManagedReference("ScheduleScheduledEvent")
    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<ScheduledEvent> scheduledEvents = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScheduledEvent> getScheduledEvents() {
        return scheduledEvents;
    }

    public void setScheduledEvents(List<ScheduledEvent> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
    }
}
