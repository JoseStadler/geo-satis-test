package ec.com.jasr.geosatisschedule.modules.schedule.core.repository;

import ec.com.jasr.geosatisschedule.modules.schedule.core.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
