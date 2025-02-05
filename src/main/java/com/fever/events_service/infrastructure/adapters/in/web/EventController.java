package com.fever.events_service.infrastructure.adapters.in.web;

import com.fever.events_service.application.services.EventService;
import com.fever.events_service.infrastructure.adapters.in.web.dto.EventDTO;
import com.fever.events_service.infrastructure.adapters.in.web.dto.EventResponseDTO;
import com.fever.events_service.infrastructure.adapters.in.web.mapper.EventDTOMapper;
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
    private final EventDTOMapper eventDTOMapper;

    public EventController(EventService eventService, EventDTOMapper eventDTOMapper) {
        this.eventService = eventService;
        this.eventDTOMapper = eventDTOMapper;
    }

    @GetMapping("/search")
    public ResponseEntity<EventResponseDTO> searchEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<EventDTO> eventDTOs = eventDTOMapper.toEventDTOList(eventService.searchEvents(startDate, endDate));

        EventResponseDTO response = new EventResponseDTO(new EventResponseDTO.DataDTO(eventDTOs), null);

        return ResponseEntity.ok(response);
    }
}
