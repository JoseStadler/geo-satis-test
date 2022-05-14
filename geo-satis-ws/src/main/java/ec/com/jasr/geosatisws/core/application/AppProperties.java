package ec.com.jasr.geosatisws.core.application;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class AppProperties {

    @Value("${geoatis.config.allowed-origins-authorized}")
    private @Getter String[] allowedOriginsAuthorized;

    @Value("${spring.flyway.baseline-on-migrate}")
    private @Getter Boolean flywayBaselineOnMigrate;

    @Value("${spring.flyway.out-of-order}")
    private @Getter Boolean flywayOutOfOrder;

    @Value("${geoatis.file-system-path}")
    private @Getter String fileSystemPath;
}
