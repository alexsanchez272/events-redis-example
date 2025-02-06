package com.fever.events_service.infrastructure.adapters.in.web;

import com.fever.events_service.application.services.EventService;
import com.fever.events_service.infrastructure.adapters.in.web.dto.EventDTO;
import com.fever.events_service.infrastructure.adapters.in.web.dto.EventResponseDTO;
import com.fever.events_service.infrastructure.adapters.in.web.mapper.EventDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Event", description = "Event search API")
public class EventController {

    private final EventService eventService;
    private final EventDTOMapper eventDTOMapper;

    public EventController(EventService eventService, EventDTOMapper eventDTOMapper) {
        this.eventService = eventService;
        this.eventDTOMapper = eventDTOMapper;
    }

    @Operation(summary = "Search events", description = "Search for events based on start and end date")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = EventResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "The request was not correctly formed (missing required parameters, wrong types...)",
            content = @Content(schema = @Schema(implementation = EventResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = EventResponseDTO.class)))
    @GetMapping("/search")
    public ResponseEntity<EventResponseDTO> searchEvents(
            @Parameter(description = "Start date and time (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<EventDTO> eventDTOs = eventDTOMapper.toEventDTOList(eventService.searchEvents(startDate, endDate));

        EventResponseDTO response = new EventResponseDTO(new EventResponseDTO.DataDTO(eventDTOs), null);

        return ResponseEntity.ok(response);
    }
}
