package com.fever.events_service.application;

import com.fever.events_service.application.usecases.SearchEventsUseCaseImpl;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.out.EventCachePort;
import com.fever.events_service.domain.ports.out.EventPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.fever.events_service.infrastructure.adapter.TestDataFactory.createEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SearchEventsUseCaseImplTest {

    @Mock
    private EventCachePort eventCache;

    @Mock
    private EventPersistencePort eventRepository;

    private SearchEventsUseCaseImpl searchEventsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchEventsUseCase = new SearchEventsUseCaseImpl(eventCache, eventRepository);
    }

    @Test
    void shouldReturnEventsFromCacheWhenAvailable() {
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 31, 0, 0);
        List<Event> expectedEvents = Arrays.asList(
                createEvent("291"),
                createEvent("322"),
                createEvent("1591")
        );

        when(eventCache.getCachedEvents(startDate, endDate)).thenReturn(Optional.of(expectedEvents));

        List<Event> result = searchEventsUseCase.searchEvents(startDate, endDate);

        assertEquals(expectedEvents, result);
        verify(eventCache).getCachedEvents(startDate, endDate);
        verifyNoInteractions(eventRepository);
    }

    @Test
    void shouldFetchEventsFromRepositoryWhenNotInCache() {
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 31, 0, 0);
        List<Event> expectedEvents = Arrays.asList(
                createEvent("291"),
                createEvent("322"),
                createEvent("1591")
        );

        when(eventCache.getCachedEvents(startDate, endDate)).thenReturn(Optional.empty());
        when(eventRepository.fetchEvents(startDate, endDate)).thenReturn(expectedEvents);

        List<Event> result = searchEventsUseCase.searchEvents(startDate, endDate);

        assertEquals(expectedEvents, result);
        verify(eventCache).getCachedEvents(startDate, endDate);
        verify(eventRepository).fetchEvents(startDate, endDate);
        verify(eventCache).cacheEvents(startDate, endDate, expectedEvents);
    }

    @Test
    void shouldCacheEventsAfterFetchingFromRepository() {
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 31, 0, 0);
        List<Event> expectedEvents = Arrays.asList(
                createEvent("291"),
                createEvent("322"),
                createEvent("1591")
        );

        when(eventCache.getCachedEvents(startDate, endDate)).thenReturn(Optional.empty());
        when(eventRepository.fetchEvents(startDate, endDate)).thenReturn(expectedEvents);

        searchEventsUseCase.searchEvents(startDate, endDate);

        verify(eventCache).cacheEvents(startDate, endDate, expectedEvents);
    }

    @Test
    void shouldReturnEmptyListWhenNoEventsFound() {
        LocalDateTime startDate = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 0, 0);

        when(eventCache.getCachedEvents(startDate, endDate)).thenReturn(Optional.empty());
        when(eventRepository.fetchEvents(startDate, endDate)).thenReturn(Collections.emptyList());

        List<Event> result = searchEventsUseCase.searchEvents(startDate, endDate);

        assertEquals(0, result.size());
        verify(eventCache).getCachedEvents(startDate, endDate);
        verify(eventRepository).fetchEvents(startDate, endDate);
        verify(eventCache).cacheEvents(startDate, endDate, Collections.emptyList());
    }
}
