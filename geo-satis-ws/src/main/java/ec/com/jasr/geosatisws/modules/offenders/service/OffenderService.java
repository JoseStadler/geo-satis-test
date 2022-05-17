package ec.com.jasr.geosatisws.modules.offenders.service;

import ec.com.jasr.geosatisws.core.application.AppSpringCtx;
import ec.com.jasr.geosatisws.core.util.AppException;
import ec.com.jasr.geosatisws.core.util.AppUtil;
import ec.com.jasr.geosatisws.modules.common.filesystem.service.FileSystemService;
import ec.com.jasr.geosatisws.modules.offenders.model.dto.OffenderDTO;
import ec.com.jasr.geosatisws.modules.offenders.model.dto.TrackedOffender;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OffenderService {

    private static List<TrackedOffender> trackedOffenders = new ArrayList<>();

    @Autowired
    private OffenderRepository offenderRepo;

    public Page<Offender> findOffendersPaged(int page, int size) {
        Page<Offender> pagedList = offenderRepo.findAll(AppUtil.createPageRequest(page, size, Sort.by(Sort.Direction.ASC, "lastName")));

        this.startTrackingOffenders(pagedList);

        return pagedList;
    }

    private void startTrackingOffenders(Page<Offender> pagedList) {
        pagedList.getContent().stream().forEach((offender) -> {
            if (!this.offenderAlreadyTracked(offender)) {
                this.trackNewOffender(offender);
            } else {
                this.updateTrackedOffender(offender);
            }
        });
    }

    private boolean offenderAlreadyTracked(Offender offender) {
        return trackedOffenders.stream().anyMatch(trackedOffender -> trackedOffender.getOffender().getId().equals(offender.getId()));
    }

    private void trackNewOffender(Offender offender) {
        OffenderDTO offenderDTO = new OffenderDTO(offender);
        TrackedOffender trackedOffender = new TrackedOffender(offenderDTO);
        trackedOffender.beginTracking();
        trackedOffenders.add(trackedOffender);
    }

    private void updateTrackedOffender(Offender offender) {
        TrackedOffender foundOffender = trackedOffenders.stream().filter(trackedOffender -> trackedOffender.getOffender().getId().equals(offender.getId())).collect(Collectors.toList()).get(0);
        foundOffender.setOffender(new OffenderDTO(offender));
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
        this.stopTrackedOffender(optionalSavedOffender.get().getId());
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

    private void stopTrackedOffender(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        this.stopTrackedOffenders(ids);
    }
    public void stopTrackedOffenders(List<Long> offenderIds) {
        trackedOffenders.stream()
                .filter(trackedOffender ->
                        offenderIds.stream()
                                .anyMatch(id -> trackedOffender.getOffender().getId().equals(id)))
                .forEach(trackedOffender -> trackedOffender.stopTracking());
        trackedOffenders.removeAll(
                trackedOffenders.stream()
                        .filter(trackedOffender -> trackedOffender.isShutdown())
                        .collect(Collectors.toList())
        );
    }
}
