package com.fever.events_service.domain.ports.out;

import com.fever.events_service.domain.models.Event;

import java.util.List;

public interface EventProviderPort {
    List<Event> fetchEvents();
}
