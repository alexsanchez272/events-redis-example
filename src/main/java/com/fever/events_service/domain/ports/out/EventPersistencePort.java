package com.fever.events_service.domain.ports.out;

import com.fever.events_service.domain.models.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventPersistencePort {

    List<Event> fetchEvents(LocalDateTime startsAt, LocalDateTime endsAt);

    Set<String> fetchActiveEventIds();

    void markEventsAsInactiveByIds(Set<String> obsoleteIds);

    void saveOrUpdateEvents(List<Event> events);

    void saveEvent (Event event);
}
