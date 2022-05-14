package ec.com.jasr.geosatisws.core.security.util;
import java.util.HashMap;

import org.springframework.http.HttpHeaders;

public class SecurityConstants {

    public static final HashMap<String, String> CORS_HEADERS = new HashMap<String, String>();

    static {
        CORS_HEADERS.put(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        CORS_HEADERS.put(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, PATCH, GET, OPTIONS, DELETE");
        CORS_HEADERS.put(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"Origin, Authorization, X-Requested-With, Content-Type, Accept, X-XSRF-TOKEN-2.0, Content-Encoding");
        CORS_HEADERS.put(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "");
    }
}