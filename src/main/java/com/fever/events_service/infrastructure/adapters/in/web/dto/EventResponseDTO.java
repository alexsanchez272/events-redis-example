package com.fever.events_service.infrastructure.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fever.events_service.domain.models.error.Error;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EventResponseDTO(DataDTO data, Error error) {
    public record DataDTO(List<EventDTO> events) { }
}
