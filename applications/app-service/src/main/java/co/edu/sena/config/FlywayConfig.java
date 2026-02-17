package co.edu.sena.config;

import co.edu.sena.r2dbc.config.MySqlConnectionProperties;
import org.flywaydb.core.Flyway;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MySqlConnectionProperties.class)
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(MySqlConnectionProperties properties) {
        return Flyway.configure()
                .dataSource(
                        "jdbc:mysql://" + properties.host() + ":" + properties.port() + "/"
                                + properties.database(),
                        properties.username(),
                        properties.password())
                .load();
    }
}
