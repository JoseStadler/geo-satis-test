package ec.com.jasr.geosatisschedule.core.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    public ResponseEntity<Object> exceptionControllerManagement(Exception ex) {
        if (ex instanceof AppException) {
            return new ResponseEntity<>(ex.getMessage(), ((AppException) ex).getCode());
        }
        ex.printStackTrace();
        return new ResponseEntity<>("There was an error, try again later", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
