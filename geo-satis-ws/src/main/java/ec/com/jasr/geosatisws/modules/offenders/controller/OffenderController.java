package ec.com.jasr.geosatisws.modules.offenders.controller;

import ec.com.jasr.geosatisws.core.util.AppException;
import ec.com.jasr.geosatisws.modules.offenders.model.entity.Offender;
import ec.com.jasr.geosatisws.modules.offenders.service.OffenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Object> saveOffender(@RequestPart Offender offender, @RequestPart(required = false) MultipartFile file) {
        try {
            return new ResponseEntity<>(offenderService.saveOffender(offender), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "{id}")
    public ResponseEntity<Object> updateOffender(@PathVariable Long id, @RequestPart Offender offender, @RequestPart(required = false) MultipartFile file) {
        try {
            return new ResponseEntity<>(offenderService.updateOffender(id, offender), HttpStatus.OK);
        } catch (Exception ex) {
            if (ex instanceof AppException) {
                return new ResponseEntity<>(ex.getMessage(), ((AppException) ex).getCode());
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
