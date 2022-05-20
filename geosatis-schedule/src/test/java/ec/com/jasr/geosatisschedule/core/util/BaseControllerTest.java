package ec.com.jasr.geosatisschedule.core.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class BaseControllerTest {

    @Test
    void ShouldResponseWithAppExceptionMessageAndCode() {
        BaseController baseController = new BaseController();
        ResponseEntity<Object> response = baseController.exceptionControllerManagement(new AppException("Test", HttpStatus.BAD_REQUEST));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Test", response.getBody());
    }

    @Test
    void ShouldResponseWithInternalServerErrorAndMessage() {
        BaseController baseController = new BaseController();
        ResponseEntity<Object> response = baseController.exceptionControllerManagement(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("There was an error, try again later", response.getBody());
    }
}