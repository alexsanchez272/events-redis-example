package com.fever.events_service.domain.ports.out;

import com.fever.events_service.domain.models.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventCachePort {

    /**
     * Caches a list of events for a specific date range.
     *
     * @param startsAt the start date of the range.
     * @param endsAt the end date of the range.
     * @param events the list of events to cache.
     */
    void cacheEvents(LocalDateTime startsAt, LocalDateTime endsAt, List<Event> events);

    /**
     * Retrieves cached events for a specific date range.
     *
     * @param startsAt the start date of the range.
     * @param endsAt the end date of the range.
     * @return an Optional containing the list of events if cached, or empty if not.
     */
    Optional<List<Event>> getCachedEvents(LocalDateTime startsAt, LocalDateTime endsAt);

    /**
     * Invalidates the cache for a specific date range.
     *
     * @param startsAt the start date of the range to invalidate.
     * @param endsAt the end date of the range to invalidate.
     */
    void invalidateCache(LocalDateTime startsAt, LocalDateTime endsAt);

    /**
     * Caches an individual event.
     *
     * @param event the event to cache.
     */
    void cacheEvent(Event event);

    /**
     * Retrieves an individual event from the cache.
     *
     * @param eventId the ID of the event to retrieve.
     * @return an Optional containing the event if cached, or empty if not.
     */
    Optional<Event> getCachedEvent(String eventId);

    /**
     * Invalidates the cache for a specific event.
     *
     * @param eventId the ID of the event whose cache should be invalidated.
     */
    void invalidateEventCache(String eventId);
}
