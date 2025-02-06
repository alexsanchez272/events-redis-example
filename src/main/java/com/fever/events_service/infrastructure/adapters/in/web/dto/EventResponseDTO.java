package com.fever.events_service.infrastructure.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Event search response")
public record EventResponseDTO(
        @Schema(description = "Response data containing list of events")
        DataDTO data,

        @Schema(description = "Error information, if any")
        ErrorDTO error
) {
    @Schema(description = "Data containing list of events")
    public record DataDTO(
            @Schema(description = "List of events matching the search criteria")
            List<EventDTO> events
    ) { }
}
