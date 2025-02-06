package com.fever.events_service.infrastructure.adapters.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error information")
public record ErrorDTO(
        @Schema(description = "Error code")
        String code,

        @Schema(description = "Error message")
        String message
) {}
