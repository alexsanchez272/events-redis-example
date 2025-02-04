package com.fever.events_service.infrastructure.adapter.out.persistence;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.models.Zone;
import com.fever.events_service.infrastructure.adapter.TestDataFactory;
import com.fever.events_service.infrastructure.adapters.out.persistence.EventMapper;
import com.fever.events_service.infrastructure.adapters.out.persistence.EventPersistenceAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({EventPersistenceAdapter.class, EventMapper.class})
@ActiveProfiles("test")
class EventPersistenceAdapterIntegrationTest {

    @Autowired
    private EventPersistenceAdapter eventPersistenceAdapter;

    @Test
    void shouldPersistAndRetrieveEventsCorrectly() {
        List<Event> events = TestDataFactory.createMultipleTestEvents();

        events.forEach(eventPersistenceAdapter::saveEvent);

        LocalDateTime startDate = LocalDateTime.parse("2020-01-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2022-12-31T23:59:59");
        List<Event> retrievedEvents = eventPersistenceAdapter.fetchEvents(startDate, endDate);

        assertEquals(3, retrievedEvents.size());

        for (int i = 0; i < events.size(); i++) {
            Event originalEvent = events.get(i);
            Event loadedEvent = retrievedEvents.get(i);

            assertEquals(originalEvent.getBaseEventId(), loadedEvent.getBaseEventId());
            assertEquals(originalEvent.getEventId(), loadedEvent.getEventId());
            assertEquals(originalEvent.getTitle(), loadedEvent.getTitle());
            assertEquals(originalEvent.getSellMode(), loadedEvent.getSellMode());
            assertEquals(originalEvent.getOrganizerCompanyId(), loadedEvent.getOrganizerCompanyId());
            assertEquals(originalEvent.getEventStartDate(), loadedEvent.getEventStartDate());
            assertEquals(originalEvent.getEventEndDate(), loadedEvent.getEventEndDate());
            assertEquals(originalEvent.getSellFrom(), loadedEvent.getSellFrom());
            assertEquals(originalEvent.getSellTo(), loadedEvent.getSellTo());
            assertEquals(originalEvent.isSoldOut(), loadedEvent.isSoldOut());
            assertEquals(originalEvent.getZones().size(), loadedEvent.getZones().size());

            for (int j = 0; j < originalEvent.getZones().size(); j++) {
                Zone originalZone = originalEvent.getZones().get(j);
                Zone loadedZone = loadedEvent.getZones().get(j);

                assertEquals(originalZone.getZoneId(), loadedZone.getZoneId());
                assertEquals(originalZone.getName(), loadedZone.getName());
                assertEquals(originalZone.getMinCapacity(), loadedZone.getMinCapacity());
                assertEquals(originalZone.getMaxCapacity(), loadedZone.getMaxCapacity());
                assertEquals(originalZone.getMinPrice(), loadedZone.getMinPrice());
                assertEquals(originalZone.getMaxPrice(), loadedZone.getMaxPrice());
                assertEquals(originalZone.isNumbered(), loadedZone.isNumbered());
            }
        }
    }
}
