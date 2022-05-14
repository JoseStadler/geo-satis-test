package ec.com.jasr.geosatisws;

import ec.com.jasr.geosatisws.core.application.AppProperties;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan
public class GeoSatisWsApplication implements CommandLineRunner {

	@Autowired
	DataSource dataSource;

	@Autowired
	AppProperties appProperties;

	public static void main(String[] args) {
		SpringApplication.run(GeoSatisWsApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Flyway.configure()
			.schemas("public")
			.locations("db")
			.baselineOnMigrate(appProperties.getFlywayBaselineOnMigrate())
			.outOfOrder(appProperties.getFlywayOutOfOrder())
			.ignoreMissingMigrations(true)
			.dataSource(dataSource).load().migrate();
	}

}
