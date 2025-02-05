package com.fever.events_service.infrastructure.adapter.out.cache;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.infrastructure.adapter.TestDataFactory;
import com.fever.events_service.infrastructure.adapters.out.cache.EventCacheAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventCacheAdapterTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private EventCacheAdapter eventCacheAdapter;

    private final long cacheTtl = 3600;
    private final String cacheIdentifier = "test_cache";

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        eventCacheAdapter = new EventCacheAdapter(redisTemplate, cacheTtl, cacheIdentifier);
    }

    @Test
    void shouldPersistEventsCorrectly() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        List<Event> events = TestDataFactory.createMultipleTestEvents();

        eventCacheAdapter.cacheEvents(start, end, events);

        String expectedKey = cacheIdentifier + ":range:" + formatter.format(start) + ":" + formatter.format(end);

        verify(valueOperations).set(eq(expectedKey), eq(events), eq(cacheTtl), eq(TimeUnit.SECONDS));
    }

    @Test
    void shouldRetrieveCachedEventsWhenTheyExist() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        List<Event> events = TestDataFactory.createMultipleTestEvents();

        String expectedKey = cacheIdentifier + ":range:" + formatter.format(start) + ":" + formatter.format(end);
        when(valueOperations.get(eq(expectedKey))).thenReturn(events);

        Optional<List<Event>> result = eventCacheAdapter.getCachedEvents(start, end);

        assertTrue(result.isPresent());
        assertEquals(events, result.get());
    }

    @Test
    void shouldReturnEmptyOptionalWhenNoCachedEvents() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        String expectedKey = cacheIdentifier + ":range:" + formatter.format(start) + ":" + formatter.format(end);
        when(valueOperations.get(eq(expectedKey))).thenReturn(null);

        Optional<List<Event>> result = eventCacheAdapter.getCachedEvents(start, end);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldInvalidateCacheForRangeCorrectly() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        String expectedKey = cacheIdentifier + ":range:" + formatter.format(start) + ":" + formatter.format(end);
        eventCacheAdapter.invalidateCache(start, end);

        verify(redisTemplate).delete(eq(expectedKey));
    }

    @Test
    void shouldPersistSingleEventCorrectly() {
        Event event = TestDataFactory.createEvent("291");
        eventCacheAdapter.cacheEvent(event);

        String expectedKey = cacheIdentifier + ":event:" + event.getBaseEventId();
        verify(valueOperations).set(eq(expectedKey), eq(event), eq(cacheTtl), eq(TimeUnit.SECONDS));
    }

    @Test
    void shouldRetrieveCachedEventWhenItExists() {
        Event event = TestDataFactory.createEvent("291");
        String expectedKey = cacheIdentifier + ":event:" + event.getBaseEventId();
        when(valueOperations.get(eq(expectedKey))).thenReturn(event);

        Optional<Event> result = eventCacheAdapter.getCachedEvent(event.getBaseEventId());

        assertTrue(result.isPresent());
        assertEquals(event, result.get());
    }

    @Test
    void shouldReturnEmptyOptionalWhenCachedEventNotFound() {
        String eventId = "999";
        String expectedKey = cacheIdentifier + ":event:" + eventId;
        when(valueOperations.get(eq(expectedKey))).thenReturn(null);

        Optional<Event> result = eventCacheAdapter.getCachedEvent(eventId);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldInvalidateSingleEventCacheCorrectly() {
        String eventId = "291";
        String expectedKey = cacheIdentifier + ":event:" + eventId;

        eventCacheAdapter.invalidateEventCache(eventId);

        verify(redisTemplate).delete(eq(expectedKey));
    }
}
