package com.fever.events_service.domain.ports.out;

import com.fever.events_service.domain.models.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface FetchEventPort {

    List<Event> fetchEvents(LocalDateTime startsAt, LocalDateTime endsAt);
}
