package com.fever.events_service.infrastructure.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * ObjectMapper for the web layer (API) without default typing.
     * Used by default in Spring Boot JSON conversions.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // No se activa default typing aquí para que el JSON enviado a la API sea limpio
        return objectMapper;
    }
}
