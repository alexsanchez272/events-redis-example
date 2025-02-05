package com.fever.events_service.infrastructure.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import io.github.resilience4j.common.bulkhead.configuration.BulkheadConfigCustomizer;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {

    private static final String PROVIDER_SERVICE = "providerService";

    @Bean
    public CircuitBreakerConfigCustomizer providerServiceCircuitBreakerConfig() {
        return CircuitBreakerConfigCustomizer
                .of(PROVIDER_SERVICE,
                        builder -> builder
                                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                                .failureRateThreshold(50.0f)
                                .permittedNumberOfCallsInHalfOpenState(10)
                                .slidingWindowSize(100)
                                .minimumNumberOfCalls(10)
                                .recordException(throwable ->
                                        !(throwable instanceof IllegalArgumentException))
                );
    }

    @Bean
    public RetryConfigCustomizer providerServiceRetryConfig() {
        return RetryConfigCustomizer
                .of(PROVIDER_SERVICE,
                        builder -> builder
                                .maxAttempts(3)
                                .waitDuration(Duration.ofSeconds(1))
                                .retryExceptions(Exception.class)
                                .ignoreExceptions(IllegalArgumentException.class)
                );
    }

    @Bean
    public BulkheadConfigCustomizer providerServiceBulkheadConfig() {
        return BulkheadConfigCustomizer
                .of(PROVIDER_SERVICE,
                        builder -> builder
                                .maxConcurrentCalls(10)
                                .maxWaitDuration(Duration.ofMillis(10))
                );
    }
}
