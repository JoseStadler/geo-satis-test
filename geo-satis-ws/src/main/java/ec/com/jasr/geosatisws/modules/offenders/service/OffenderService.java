package ec.com.jasr.geosatisws.modules.offenders.service;

import ec.com.jasr.geosatisws.core.util.AppException;
import ec.com.jasr.geosatisws.core.util.AppUtil;
import ec.com.jasr.geosatisws.modules.offenders.model.entity.Offender;
import ec.com.jasr.geosatisws.modules.offenders.repository.OffenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OffenderService {

    @Autowired
    private OffenderRepository offenderRepo;

    public Page<Offender> findOffendersPaged(int page, int size) {
        return offenderRepo.findAll(AppUtil.createPageRequest(page, size, Sort.by(Sort.Direction.ASC, "lastName")));
    }

    @Transactional
    public Offender saveOffender(Offender offender) {
        return offenderRepo.save(offender);
    }

    @Transactional
    public Offender updateOffender(Long id, Offender offender) throws AppException {
        Optional<Offender> optionalSavedOffender = offenderRepo.findById(id);

        if (!optionalSavedOffender.isPresent()) {
            throw new AppException("Provided Id does not exist", HttpStatus.NOT_MODIFIED);
        }

        Offender savedOffender = optionalSavedOffender.get();

        offender.setId(id);
        offender.getPosition().setId(savedOffender.getPosition().getId());

        return this.saveOffender(offender);
    }

    @MessageMapping("/hello")
    @SendTo("/ws-resp/greetings")
    public Offender greeting(String message) throws Exception {
        Thread.sleep(1000); // simulated delay
        Offender of = new Offender();
        of.setFirstName("Prueba");
        of.setLastName("ST");
        return of;
    }
}
