package ec.com.jasr.geosatisschedule.modules.schedule.core.nativequery;

import ec.com.jasr.geosatisschedule.modules.schedule.scheduleevent.model.entity.ScheduledEvent;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleNativeQuery {

    @PersistenceContext
    EntityManager em;

    public List<ScheduledEvent> getScheduledEvents(Long scheduleId, LocalDateTime date) {
        HashMap<String, Object> parameters = new HashMap<>();
        Query query = null;

        StringBuilder selectClause = new StringBuilder("SELECT e.* FROM (");
        selectClause.append("SELECT generate_series(e.start_date, '2022-12-01', ");
        selectClause.append("   (CASE WHEN re.\"interval\" IS NULL THEN '1 day' ELSE cast(Concat(re.\"interval\", ' ', re.interval_type) as INTERVAL) END)) as repeated_date, ");
        selectClause.append("   e.start_date, re.* ");
        selectClause.append("FROM schedule_event e ");
        selectClause.append("JOIN recurring_schedule_event re ON re.schedule_event = e.id");
        selectClause.append(") as mydate ");
        selectClause.append("JOIN schedule_event e ON e.id = mydate.schedule_event ");
        selectClause.append("WHERE ");
        selectClause.append("DATE(mydate.repeated_date) = '2022-11-18' ");
        selectClause.append("AND (mydate.day IS NULL OR EXTRACT(DOW FROM mydate.repeated_date) = mydate.day) ");




//        StringBuilder whereClausule = new StringBuilder(" WHERE TRUE IS TRUE ");
//        StringBuilder orderClausule = new StringBuilder(" ORDER BY rt.id ");

//        // grupo empresarial
//        if(transitClaimSearchDTO.getBusinessgroupId() != null) {
//            whereClausule.append("and pers.grupo_empresarial = :businessgroupId  ");
//            parameters.put("businessgroupId",transitClaimSearchDTO.getBusinessgroupId());
//        }


//        selectClause.append(whereClausule);
//        selectClause.append(orderClausule);


        query = em.createNativeQuery(selectClause.toString(), ScheduledEvent.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();

    }
}
