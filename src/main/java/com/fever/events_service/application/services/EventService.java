package com.fever.events_service.application.services;

import com.fever.events_service.domain.models.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<Event> searchEvents(LocalDateTime startDate, LocalDateTime endDate);
}
