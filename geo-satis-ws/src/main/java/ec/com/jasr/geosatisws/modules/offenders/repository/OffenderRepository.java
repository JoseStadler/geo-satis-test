package ec.com.jasr.geosatisws.modules.offenders.repository;

import ec.com.jasr.geosatisws.modules.offenders.model.entity.Offender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffenderRepository extends JpaRepository<Offender, Long> {

    Page<Offender> findAll(Pageable page);
}
