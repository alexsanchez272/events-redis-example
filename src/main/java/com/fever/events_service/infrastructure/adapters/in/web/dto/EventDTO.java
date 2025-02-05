package com.fever.events_service.infrastructure.adapters.in.web.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventDTO(
        String baseEventId,
        String eventId,
        String title,
        String sellMode,
        String organizerCompanyId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime sellFrom,
        LocalDateTime sellTo,
        boolean soldOut,
        List<ZoneDTO> zones
) { }
