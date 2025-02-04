package com.fever.events_service.infrastructure.adapters.out.cache;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.out.EventCachePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class EventCacheAdapter implements EventCachePort {

    private final RedisTemplate<String, Object> redisTemplate;
    private final long cacheTtl;
    private final String cacheIdentifier;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public EventCacheAdapter(
            RedisTemplate<String, Object> redisTemplate,
            @Value("${cache.ttl.seconds}") long cacheTtl,
            @Value("${cache.identifier}") String cacheIdentifier
    ) {
        this.redisTemplate = redisTemplate;
        this.cacheTtl = cacheTtl;
        this.cacheIdentifier = cacheIdentifier;
    }

    @Override
    public void cacheEvents(LocalDateTime startsAt, LocalDateTime endsAt, List<Event> events) {
        String key = generateRangeKey(startsAt, endsAt);
        redisTemplate.opsForValue().set(key, events, cacheTtl, TimeUnit.SECONDS);
    }

    @Override
    public Optional<List<Event>> getCachedEvents(LocalDateTime startsAt, LocalDateTime endsAt) {
        String key = generateRangeKey(startsAt, endsAt);
        @SuppressWarnings("unchecked")
        List<Event> events = (List<Event>) redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(events);
    }

    @Override
    public void invalidateCache(LocalDateTime startsAt, LocalDateTime endsAt) {
        String key = generateRangeKey(startsAt, endsAt);
        redisTemplate.delete(key);
    }

    @Override
    public void cacheEvent(Event event) {
        String key = generateEventKey(event.getBaseEventId());
        redisTemplate.opsForValue().set(key, event, cacheTtl, TimeUnit.SECONDS);
    }

    @Override
    public Optional<Event> getCachedEvent(String eventId) {
        String key = generateEventKey(eventId);
        Event event = (Event) redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(event);
    }

    @Override
    public void invalidateEventCache(String eventId) {
        String key = generateEventKey(eventId);
        redisTemplate.delete(key);
    }

    private String generateRangeKey(LocalDateTime startsAt, LocalDateTime endsAt) {
        return cacheIdentifier + ":range:" + DATE_FORMATTER.format(startsAt) + ":" + DATE_FORMATTER.format(endsAt);
    }

    private String generateEventKey(String eventId) {
        return cacheIdentifier + ":event:" + eventId;
    }
}
