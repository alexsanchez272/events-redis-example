package com.fever.events_service.infrastructure.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // Limpiar el esquema solo en desarrollo
            if (System.getProperty("spring.profiles.active", "").equals("dev")) {
                flyway.clean();
            }
            // Ejecutar las migraciones
            flyway.migrate();
        };
    }
}
