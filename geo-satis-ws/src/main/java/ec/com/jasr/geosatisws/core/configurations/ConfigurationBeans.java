package ec.com.jasr.geosatisws.core.configurations;

import ec.com.jasr.geosatisws.core.application.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBeans {

    @Bean
    public AppProperties applicationProperties() {
        return new AppProperties();
    }

}
