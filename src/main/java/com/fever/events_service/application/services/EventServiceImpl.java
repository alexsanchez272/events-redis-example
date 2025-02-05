package com.fever.events_service.application.services;

import com.fever.events_service.domain.exceptions.EventsNotFoundException;
import com.fever.events_service.domain.exceptions.InvalidDateRangeException;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.in.SearchEventsUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private final SearchEventsUseCase searchEventsUseCase;

    public EventServiceImpl(SearchEventsUseCase searchEventsUseCase) {
        this.searchEventsUseCase = searchEventsUseCase;
    }

    @Override
    public List<Event> searchEvents(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("process=search_events, status=init, startDate={}, endDate={}", startDate, endDate);

        validateDateRange(startDate, endDate);

        List<Event> events = searchEventsUseCase.searchEvents(startDate, endDate);

        if (events.isEmpty()) {
            log.warn("process=search_events, status=not_found, startDate={}, endDate={}", startDate, endDate);
            throw new EventsNotFoundException("No events found for the given date range");
        }

        log.info("process=search_events, status=success, startDate={}, endDate={}, eventsFound={}",
                startDate, endDate, events.size());

        return events;
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            log.error("process=search_events, status=invalid_date_range, startDate={}, endDate={}", startDate, endDate);
            throw new InvalidDateRangeException("Start date must be before end date");
        }
    }
}
