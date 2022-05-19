package ec.com.jasr.geosatisschedule.core.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AppException extends Exception {
    private final @Getter
    String message;
    private final @Getter
    HttpStatus code;

    public AppException(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }
}
