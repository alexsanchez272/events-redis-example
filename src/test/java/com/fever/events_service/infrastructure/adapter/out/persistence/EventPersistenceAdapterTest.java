package com.fever.events_service.infrastructure.adapter.out.persistence;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.infrastructure.adapters.out.persistence.*;
import com.fever.events_service.infrastructure.adapters.out.persistence.entities.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventPersistenceAdapterTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    private EventPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new EventPersistenceAdapter(eventRepository, eventMapper);
    }

    @Test
    void shouldFetchEventsAndMapEntitiesToDomainModels() {
        LocalDateTime startsAt = LocalDateTime.parse("2020-01-01T00:00:00");
        LocalDateTime endsAt = LocalDateTime.parse("2022-12-31T23:59:59");

        List<EventEntity> eventEntities = TestDataFactory.createMultipleTestEventEntities();
        List<Event> expectedEvents = TestDataFactory.createMultipleTestEvents();

        when(eventRepository.findEventsBetweenDates(startsAt, endsAt))
                .thenReturn(eventEntities);

        for (int i = 0; i < eventEntities.size(); i++) {
            when(eventMapper.toEvent(eventEntities.get(i))).thenReturn(expectedEvents.get(i));
        }

        List<Event> events = adapter.fetchEvents(startsAt, endsAt);

        assertEquals(3, events.size());
        for (int i = 0; i < events.size(); i++) {
            assertEquals(expectedEvents.get(i), events.get(i));
        }

        verify(eventRepository).findEventsBetweenDates(startsAt, endsAt);
        for (EventEntity entity : eventEntities) {
            verify(eventMapper).toEvent(entity);
        }
    }

    @Test
    void shouldConvertDomainEventToEntityAndSaveIt() {
        List<Event> events = TestDataFactory.createMultipleTestEvents();
        List<EventEntity> expectedEntities = TestDataFactory.createMultipleTestEventEntities();

        for (int i = 0; i < events.size(); i++) {
            when(eventMapper.toEventEntity(events.get(i))).thenReturn(expectedEntities.get(i));
            adapter.saveEvent(events.get(i));
        }

        for (int i = 0; i < events.size(); i++) {
            verify(eventMapper).toEventEntity(events.get(i));
            verify(eventRepository).save(expectedEntities.get(i));
        }
    }
}
