package com.fever.events_service.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class FlywayInitializationListener implements ApplicationListener<ApplicationStartedEvent> {

    private static final Logger log = LoggerFactory.getLogger(FlywayInitializationListener.class);

    private final Flyway flyway;

    public FlywayInitializationListener(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("Checking Flyway configuration...");

        // Convert Location objects to strings before joining
        String locations = Arrays.stream(flyway.getConfiguration().getLocations())
                .map(location -> location.getDescriptor())
                .collect(Collectors.joining(", "));

        log.info("Flyway migration locations: {}", locations);
        log.info("Flyway migration files: {}", flyway.info().all().length);

        // Log additional Flyway configuration details
        log.info("Flyway baseline version: {}", flyway.getConfiguration().getBaselineVersion().toString());
        log.info("Flyway baseline description: {}", flyway.getConfiguration().getBaselineDescription());
        log.info("Flyway schemas: {}", String.join(", ", flyway.getConfiguration().getSchemas()));
    }
}
