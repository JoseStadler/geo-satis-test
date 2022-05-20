package ec.com.jasr.geosatisschedule.modules.schedule.core.repository;

import ec.com.jasr.geosatisschedule.modules.schedule.core.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT count(*) > 0 " +
                    "FROM schedule_event e " +
                        "JOIN schedule s ON s.id = e.schedule AND s.id = :scheduleId " +
                        "LEFT JOIN schedule_event_exception se ON se.schedule_event = e.id " +
                        "LEFT JOIN (select generate_series(e.start_date, COALESCE(re.stop_recurring_date,DATE(:toDate)), " +
                            "(CASE WHEN re.\"interval\" IS NULL THEN '1 day' ELSE cast(Concat(re.\"interval\", ' ', re.interval_type) as INTERVAL) " +
                            "END)) as repeated_date, re.* " +
                            "FROM schedule_event e " +
                                "JOIN recurring_schedule_event re ON re.schedule_event = e.id " +
                        ") recursive_date ON e.id = recursive_date.schedule_event " +
                    "WHERE " +
                    "DATE(recursive_date.repeated_date) = DATE(:searchDate) " +
                    "AND CAST(e.start_date as time) < CAST(:searchDate as time) AND CAST(e.end_date as time) > CAST(:searchDate as time)" +
                    "AND (recursive_date.day IS NULL OR EXTRACT(DOW FROM recursive_date.repeated_date) = recursive_date.day) " +
                    "AND (se.exception_date IS NULL OR se.exception_date <> DATE(:searchDate))", nativeQuery = true)
    public Boolean hasScheduledEvent(@Param("scheduleId") Long scheduleId, @Param("searchDate") LocalDateTime searchDate, @Param("toDate") LocalDateTime toDate);
}
