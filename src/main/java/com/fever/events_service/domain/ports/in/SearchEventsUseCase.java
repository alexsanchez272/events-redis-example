package com.fever.events_service.domain.ports.in;

import com.fever.events_service.domain.models.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchEventsUseCase {

    /**
     * Searches for events within a specific date range.
     *
     * @param startDate The start date of the search range.
     * @param endDate   The end date of the search range.
     * @return A list of events that occur within the specified date range.
     */
    List<Event> searchEvents(LocalDateTime startDate, LocalDateTime endDate);
}
