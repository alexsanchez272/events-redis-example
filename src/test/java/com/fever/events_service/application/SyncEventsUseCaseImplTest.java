package com.fever.events_service.application;

import com.fever.events_service.application.usecases.SyncEventsUseCaseImpl;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.out.EventCachePort;
import com.fever.events_service.domain.ports.out.EventPersistencePort;
import com.fever.events_service.domain.ports.out.EventProviderPort;
import com.fever.events_service.infrastructure.adapter.TestDataFactory;
import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderBaseEventDTO;
import com.fever.events_service.infrastructure.adapters.out.http.mapper.ProviderEventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

class SyncEventsUseCaseImplTest {

    @Mock
    private EventProviderPort eventProviderPort;
    @Mock
    private EventPersistencePort eventPersistencePort;
    @Mock
    private EventCachePort eventCachePort;
    @Mock
    private ProviderEventMapper providerEventMapper;

    private SyncEventsUseCaseImpl syncEventsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        syncEventsUseCase = new SyncEventsUseCaseImpl(eventProviderPort, eventPersistencePort, eventCachePort);
    }

    @Test
    void testSuccessfulSynchronization() {
        // Arrange
        List<ProviderBaseEventDTO> providerEvents = TestDataFactory.createMultipleProviderBaseEventDTOs();
        List<Event> events = TestDataFactory.createMultipleTestEvents();
        Set<String> activeEventIds = new HashSet<>(Arrays.asList("291", "322", "1591"));

        when(eventProviderPort.fetchEvents()).thenReturn(events);
        when(eventPersistencePort.fetchActiveEventIds()).thenReturn(activeEventIds);

        // Act
        syncEventsUseCase.syncEvents();

        // Assert
        verify(eventPersistencePort).saveOrUpdateEvents(events);
        verify(eventPersistencePort, never()).markEventsAsInactiveByIds(any());
        verify(eventCachePort).invalidateCache(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testHandlingObsoleteEvents() {
        // Arrange
        List<Event> events = Arrays.asList(
                TestDataFactory.createEvent("291"),
                TestDataFactory.createEvent("322")
        );
        Set<String> activeEventIds = new HashSet<>(Arrays.asList("291", "322", "1591"));

        when(eventProviderPort.fetchEvents()).thenReturn(events);
        when(eventPersistencePort.fetchActiveEventIds()).thenReturn(activeEventIds);

        // Act
        syncEventsUseCase.syncEvents();

        // Assert
        verify(eventPersistencePort).markEventsAsInactiveByIds(Collections.singleton("1591"));
    }

    @Test
    void testEmptyResponseFromProvider() {
        // Arrange
        when(eventProviderPort.fetchEvents()).thenReturn(Collections.emptyList());
        when(eventPersistencePort.fetchActiveEventIds()).thenReturn(new HashSet<>());

        // Act
        syncEventsUseCase.syncEvents();

        // Assert
        verify(eventPersistencePort, never()).saveOrUpdateEvents(any());
        verify(eventPersistencePort, never()).markEventsAsInactiveByIds(any());
        verify(eventCachePort, never()).invalidateCache(any(), any());
    }

    @Test
    void testErrorHandling() {
        // Arrange
        when(eventProviderPort.fetchEvents()).thenThrow(new RuntimeException("Provider communication error"));

        // Act
        syncEventsUseCase.syncEvents();

        // Assert
        verify(eventPersistencePort, never()).saveOrUpdateEvents(any());
        verify(eventPersistencePort, never()).markEventsAsInactiveByIds(any());
        verify(eventCachePort, never()).invalidateCache(any(), any());
    }

    @Test
    void testPartialSuccess() {
        // Arrange
        List<Event> events = Arrays.asList(
                TestDataFactory.createEvent("291"),
                TestDataFactory.createEvent("322"),
                TestDataFactory.createEvent("1591")
        );
        Set<String> activeEventIds = new HashSet<>(Arrays.asList("291", "322", "1591", "1234"));

        when(eventProviderPort.fetchEvents()).thenReturn(events);
        when(eventPersistencePort.fetchActiveEventIds()).thenReturn(activeEventIds);
        doThrow(new RuntimeException("Error updating event")).when(eventPersistencePort).saveOrUpdateEvents(any());

        // Act
        syncEventsUseCase.syncEvents();

        // Assert
        verify(eventPersistencePort).saveOrUpdateEvents(events);
        verify(eventPersistencePort).markEventsAsInactiveByIds(Collections.singleton("1234"));
        verify(eventCachePort, never()).invalidateCache(any(), any());
    }

    @Test
    void testDataIntegrity() {
        // Arrange
        Event event = TestDataFactory.createEvent("1591");
        List<Event> events = Collections.singletonList(event);

        when(eventProviderPort.fetchEvents()).thenReturn(events);
        when(eventPersistencePort.fetchActiveEventIds()).thenReturn(new HashSet<>());

        // Act
        syncEventsUseCase.syncEvents();

        // Assert
        verify(eventPersistencePort).saveOrUpdateEvents(events);
    }

    @Test
    void testVariousDataScenarios() {
        // Arrange
        List<Event> events = Arrays.asList(
                TestDataFactory.createEvent("291"), // Event with single zone
                TestDataFactory.createEvent("1591"), // Event with multiple zones
                TestDataFactory.createEvent("special", "Midnight Event",
                        LocalDateTime.of(2021, 12, 31, 23, 0),
                        LocalDateTime.of(2022, 1, 1, 1, 0),
                        true), // Event spanning midnight
                TestDataFactory.createEvent("long", "Long Duration Event",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(7),
                        true) // Event with long duration
        );

        when(eventProviderPort.fetchEvents()).thenReturn(events);
        when(eventPersistencePort.fetchActiveEventIds()).thenReturn(new HashSet<>());

        // Act
        syncEventsUseCase.syncEvents();

        // Assert
        verify(eventPersistencePort).saveOrUpdateEvents(events);
    }
}
