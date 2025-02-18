package com.fever.events_service.infrastructure.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Event information")
public record EventDTO(
        @Schema(description = "Base event identifier")
        String baseEventId,

        @Schema(description = "Unique event identifier")
        String eventId,

        @Schema(description = "Title of the event")
        String title,

        @Schema(description = "Selling mode of the event")
        String sellMode,

        @Schema(description = "Organizer company identifier")
        String organizerCompanyId,

        @Schema(description = "Start date and time of the event")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startDate,

        @Schema(description = "End date and time of the event")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endDate,

        @Schema(description = "Date and time when tickets start selling")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime sellFrom,

        @Schema(description = "Date and time when tickets stop selling")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime sellTo,

        @Schema(description = "Indicates if the event is sold out")
        boolean soldOut,

        @Schema(description = "List of zones available for the event")
        List<ZoneDTO> zones
) { }