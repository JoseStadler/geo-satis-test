package ec.com.jasr.geosatisws.modules.offenders.controller;

import ec.com.jasr.geosatisws.core.application.AppSpringCtx;
import ec.com.jasr.geosatisws.core.util.AppException;
import ec.com.jasr.geosatisws.modules.common.filesystem.service.FileSystemService;
import ec.com.jasr.geosatisws.modules.offenders.model.entity.Offender;
import ec.com.jasr.geosatisws.modules.offenders.service.OffenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@Controller
@RestController
@RequestMapping(value = "api/offenders")
public class OffenderController {

    @Autowired
    private OffenderService offenderService;

    @GetMapping
    public ResponseEntity<Object> findOffendersPaged(@RequestParam int page, @RequestParam int pageSize) {
        try {
            return new ResponseEntity<>(offenderService.findOffendersPaged(page, pageSize), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Object> saveOffender(@RequestPart Offender offender, @RequestPart(required = false) MultipartFile picture) {
        try {
            return new ResponseEntity<>(offenderService.saveOffender(offender, picture), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "{id}")
    public ResponseEntity<Object> updateOffender(@PathVariable Long id, @RequestPart Offender offender, @RequestPart(required = false) MultipartFile picture) {
        try {
            return new ResponseEntity<>(offenderService.updateOffender(id, offender, picture), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (ex instanceof AppException) {
                return new ResponseEntity<>(ex.getMessage(), ((AppException) ex).getCode());
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="findPhoto/{fileName:.+}")
    public ResponseEntity<?> findPhoto(@PathVariable String fileName) {
        try {
            Resource resource = AppSpringCtx.getBean(FileSystemService.class).findPhoto(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
            return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @MessageMapping("/stopTrackedOffenders")
    public void greeting(List<Long> ids) throws Exception {
        offenderService.stopTrackedOffenders(ids);
    }
}
