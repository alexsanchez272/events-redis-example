package com.fever.events_service.infrastructure.adapters.out.http;

import com.fever.events_service.domain.exceptions.ProviderCommunicationException;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.out.EventProviderPort;
import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderEventListDTO;
import com.fever.events_service.infrastructure.adapters.out.http.mapper.ProviderEventMapper;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class EventProviderAdapter implements EventProviderPort {

    private static final String PROVIDER_SERVICE = "providerService";

    private final ProviderApi providerApi;
    private final ProviderEventMapper providerEventMapper;
    private final MeterRegistry meterRegistry;
    private final Timer apiCallTimer;
    private final Counter totalCallsCounter;
    private final Counter failedCallsCounter;

    public EventProviderAdapter(ProviderApi providerApi,
                                ProviderEventMapper providerEventMapper,
                                MeterRegistry meterRegistry) {
        this.providerApi = providerApi;
        this.providerEventMapper = providerEventMapper;
        this.meterRegistry = meterRegistry;

        this.apiCallTimer = Timer.builder("provider.api.call.duration")
                .description("Time taken for provider API call")
                .register(meterRegistry);
        this.totalCallsCounter = Counter.builder("provider.api.calls.total")
                .description("Total number of API calls")
                .register(meterRegistry);
        this.failedCallsCounter = Counter.builder("provider.api.calls.failed")
                .description("Number of failed API calls")
                .register(meterRegistry);
    }

    @Override
    @CircuitBreaker(name = PROVIDER_SERVICE, fallbackMethod = "fallbackFetchEvents")
    @Retry(name = PROVIDER_SERVICE)
    @Bulkhead(name = PROVIDER_SERVICE)
    public List<Event> fetchEvents() {
        return apiCallTimer.record(() -> {
            totalCallsCounter.increment();
            try {
                log.info("process=fetch_events, status=init");
                Response<ProviderEventListDTO> response = providerApi.fetchEvents().execute();
                if (response.isSuccessful() && response.body() != null) {
                    ProviderEventListDTO eventListDTO = response.body();
                    if (eventListDTO.getOutput() != null && eventListDTO.getOutput().getBaseEvents() != null) {
                        List<Event> events = providerEventMapper.mapToEvents(eventListDTO.getOutput().getBaseEvents());
                        log.info("process=fetch_events, status=success, eventCount={}", events.size());
                        return events;
                    } else {
                        log.warn("process=fetch_events, status=empty_response");
                        return Collections.emptyList();
                    }
                } else {
                    failedCallsCounter.increment();
                    log.error("process=fetch_events, status=error, errorCode={}", response.code());
                    throw new ProviderCommunicationException("Unsuccessful response from provider");
                }
            } catch (Exception e) {
                failedCallsCounter.increment();
                log.error("process=fetch_events, status=error, errorMessage={}", e.getMessage());
                throw new ProviderCommunicationException("Error communicating with provider", e);
            }
        });
    }

    private List<Event> fallbackFetchEvents(Exception e) {
        failedCallsCounter.increment();
        log.warn("process=fetch_events, status=fallback, errorMessage={}", e.getMessage());
        return Collections.emptyList();
    }
}