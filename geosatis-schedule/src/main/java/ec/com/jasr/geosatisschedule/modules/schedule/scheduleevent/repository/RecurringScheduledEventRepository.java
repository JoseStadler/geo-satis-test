package ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.repository;

import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity.RecurringScheduledEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurringScheduledEventRepository extends JpaRepository<RecurringScheduledEvent, Long> {
}
