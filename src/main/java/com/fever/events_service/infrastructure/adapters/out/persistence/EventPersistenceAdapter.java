package com.fever.events_service.infrastructure.adapters.out.persistence;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.out.EventPersistencePort;
import com.fever.events_service.infrastructure.adapters.out.persistence.entities.EventEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EventPersistenceAdapter implements EventPersistencePort {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventPersistenceAdapter (EventRepository eventRepository, EventMapper eventMapper){
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<Event> fetchEvents(LocalDateTime startsAt, LocalDateTime endsAt) {
        return eventRepository.findEventsBetweenDates(startsAt, endsAt).stream()
                .map(eventMapper::toEvent)
                .collect(Collectors.toList());    }

    @Override
    public Set<String> fetchActiveEventIds() {
        return eventRepository.findActiveEventIds();
    }

    @Override
    public void markEventsAsInactiveByIds(Set<String> obsoleteIds) {
        eventRepository.updateEventsActiveStatus(obsoleteIds, false);
    }

    @Override
    public void saveOrUpdateEvents(List<Event> events) {
        List<EventEntity> eventEntities = events.stream()
                .map(eventMapper::toEventEntity)
                .peek(entity -> entity.setAvailable(true))
                .collect(Collectors.toList());
        eventRepository.saveAll(eventEntities);
    }

    @Override
    public void saveEvent(Event event) {
        eventRepository.save(
                eventMapper.toEventEntity(event)
        );
    }
}
