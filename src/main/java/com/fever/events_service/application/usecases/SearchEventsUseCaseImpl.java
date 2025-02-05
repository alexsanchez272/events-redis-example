package com.fever.events_service.application.usecases;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.in.SearchEventsUseCase;
import com.fever.events_service.domain.ports.out.EventCachePort;
import com.fever.events_service.domain.ports.out.EventPersistencePort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class SearchEventsUseCaseImpl implements SearchEventsUseCase {

    private final EventCachePort eventCache;
    private final EventPersistencePort eventRepository;

    public SearchEventsUseCaseImpl(EventCachePort eventCache, EventPersistencePort eventRepository) {
        this.eventCache = eventCache;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> searchEvents(LocalDateTime startDate, LocalDateTime endDate) {
        Optional<List<Event>> cachedEvents = eventCache.getCachedEvents(startDate, endDate);

        if (cachedEvents.isPresent()) {
            return cachedEvents.get();
        }

        List<Event> events = eventRepository.fetchEvents(startDate, endDate);
        eventCache.cacheEvents(startDate, endDate, events);

        return events;
    }
}
