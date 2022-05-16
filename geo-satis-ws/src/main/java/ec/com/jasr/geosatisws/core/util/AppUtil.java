package ec.com.jasr.geosatisws.core.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class AppUtil {

    public static PageRequest createPageRequest(int page, int size) {
        return PageRequest.of(page, size);
    }

    public static PageRequest createPageRequest(int page, int size, Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

}
