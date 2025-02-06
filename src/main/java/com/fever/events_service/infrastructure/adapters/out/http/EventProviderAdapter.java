package com.fever.events_service.infrastructure.adapters.out.http;

import com.fever.events_service.domain.exceptions.ProviderCommunicationException;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.out.EventProviderPort;
import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderEventListDTO;
import com.fever.events_service.infrastructure.adapters.out.http.mapper.ProviderEventMapper;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class EventProviderAdapter implements EventProviderPort {

    private static final String PROVIDER_SERVICE = "providerService";

    private final ProviderApi providerApi;
    private final ProviderEventMapper providerEventMapper;

    public EventProviderAdapter(ProviderApi providerApi, ProviderEventMapper providerEventMapper) {
        this.providerApi = providerApi;
        this.providerEventMapper = providerEventMapper;
    }

    @Override
    @CircuitBreaker(name = PROVIDER_SERVICE, fallbackMethod = "fallbackFetchEvents")
    @Retry(name = PROVIDER_SERVICE)
    @Bulkhead(name = PROVIDER_SERVICE)
    public List<Event> fetchEvents() {
        try {
            log.info("process=fetch_events, status=init");
            //TODO: Check Retrofit mapper
            Response<ProviderEventListDTO> response = providerApi.fetchEvents().execute();
            if (response.isSuccessful() && response.body() != null) {
                ProviderEventListDTO eventListDTO = response.body();
                if (eventListDTO.getOutput() != null && eventListDTO.getOutput().getBaseEvents() != null) {
                    log.info("process=fetch_events, status=success, eventCount={}", eventListDTO.getOutput().getBaseEvents().size());
                    return providerEventMapper.mapToEvents(eventListDTO.getOutput().getBaseEvents());
                } else {
                    log.warn("process=fetch_events, status=empty_response");
                    return Collections.emptyList();
                }
            } else {
                log.error("process=fetch_events, status=error, errorCode={}", response.code());
                throw new ProviderCommunicationException("Unsuccessful response from provider");
            }
        } catch (IOException e) {
            log.error("process=fetch_events, status=error, errorMessage={}", e.getMessage());
            throw new ProviderCommunicationException("Error communicating with provider", e);
        }
    }

    private List<Event> fallbackFetchEvents(Exception e) {
        log.warn("process=fetch_events, status=fallback, errorMessage={}", e.getMessage());
        return Collections.emptyList();
    }
}
