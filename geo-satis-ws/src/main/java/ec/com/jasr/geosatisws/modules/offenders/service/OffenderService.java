package ec.com.jasr.geosatisws.modules.offenders.service;

import ec.com.jasr.geosatisws.core.application.AppSpringCtx;
import ec.com.jasr.geosatisws.core.util.AppException;
import ec.com.jasr.geosatisws.core.util.AppUtil;
import ec.com.jasr.geosatisws.modules.offenders.common.filesystem.service.FileSystemService;
import ec.com.jasr.geosatisws.modules.offenders.model.entity.Offender;
import ec.com.jasr.geosatisws.modules.offenders.repository.OffenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
public class OffenderService {

    @Autowired
    private OffenderRepository offenderRepo;

    public Page<Offender> findOffendersPaged(int page, int size) {
        return offenderRepo.findAll(AppUtil.createPageRequest(page, size, Sort.by(Sort.Direction.ASC, "lastName")));
    }

    @Transactional
    public Offender saveOffender(Offender offender, MultipartFile picture) throws IOException {
        String oldPictureName = offender.getPicture();

        if (this.hasToSavePicture(picture)) {
            this.saveNewPicture(offender, picture);
            this.removeOldPicture(oldPictureName);
        }

        return offenderRepo.save(offender);
    }

    private boolean hasToSavePicture(MultipartFile picture) {
        return picture != null;
    }

    private void saveNewPicture(Offender offender, MultipartFile picture) throws IOException {
        String savedFileName = AppSpringCtx.getBean(FileSystemService.class)
                .uploadFile(offender.getLastName() + "-" + offender.getFirstName(),
                        picture);
        offender.setPicture(savedFileName);
    }

    private void removeOldPicture(String oldPictureName) {
        if (this.oldPictureExists(oldPictureName)) {
            AppSpringCtx.getBean(FileSystemService.class).remove(oldPictureName);
        }
    }

    private boolean oldPictureExists(String oldPictureName) {
        return oldPictureName != null;
    }

    @Transactional
    public Offender updateOffender(Long id, Offender offender, MultipartFile picture) throws AppException, IOException {
        Optional<Offender> optionalSavedOffender = offenderRepo.findById(id);

        if (!optionalSavedOffender.isPresent()) {
            this.throwControlledError("Provided Id does not exist", HttpStatus.NOT_MODIFIED);
        }

        this.prepareOffenderToUpdate(offender, optionalSavedOffender.get());
        return this.saveOffender(offender, picture);
    }

    private void throwControlledError(String message, HttpStatus code) throws AppException {
        throw new AppException(message, code);
    }

    private void prepareOffenderToUpdate(Offender offender, Offender savedOffender) {
        offender.setId(savedOffender.getId());
        offender.getPosition().setId(savedOffender.getPosition().getId());
        offender.setPicture(savedOffender.getPicture());
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
