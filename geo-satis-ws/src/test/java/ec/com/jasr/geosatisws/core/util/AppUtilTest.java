package ec.com.jasr.geosatisws.core.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppUtilTest {

    @Test
    void createPageRequest() {
        PageRequest pageRequest = AppUtil.createPageRequest(1,5, Sort.by(Sort.Direction.ASC, "lastName"));
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(5, pageRequest.getPageSize());
    }
}