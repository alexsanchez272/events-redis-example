package com.fever.events_service.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.fever.events_service.infrastructure.adapters.out.persistence")
@EnableTransactionManagement
public class DatabaseConfig {

}
