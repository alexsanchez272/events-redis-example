package com.fever.events_service.infrastructure.adapters.out.persistence;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.out.FetchEventPort;
import com.fever.events_service.domain.ports.out.SaveEventPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventPersistenceAdapter implements FetchEventPort, SaveEventPort {

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
    public void saveEvent(Event event) {
        eventRepository.save(
                eventMapper.toEventEntity(event)
        );
    }
}
