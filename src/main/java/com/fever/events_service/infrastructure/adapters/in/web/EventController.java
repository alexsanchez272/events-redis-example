package com.fever.events_service.infrastructure.adapters.in.web;

import com.fever.events_service.application.services.EventService;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.infrastructure.adapters.in.web.dto.EventResponseDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events/")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/search")
    public ResponseEntity<EventResponseDTO> searchEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Event> events = eventService.searchEvents(startDate, endDate);
        EventResponseDTO response = new EventResponseDTO(new EventResponseDTO.DataDTO(events), null);
        return ResponseEntity.ok(response);
    }
}
