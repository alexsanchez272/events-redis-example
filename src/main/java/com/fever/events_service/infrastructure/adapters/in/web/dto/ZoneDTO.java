package com.fever.events_service.infrastructure.adapters.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Zone information for an event")
public record ZoneDTO(
        @Schema(description = "Unique zone identifier")
        String zoneId,

        @Schema(description = "Name of the zone")
        String name,

        @Schema(description = "Minimum capacity of the zone")
        int minCapacity,

        @Schema(description = "Maximum capacity of the zone")
        int maxCapacity,

        @Schema(description = "Minimum price for tickets in this zone")
        BigDecimal minPrice,

        @Schema(description = "Maximum price for tickets in this zone")
        BigDecimal maxPrice,

        @Schema(description = "Indicates if the zone has numbered seats")
        boolean numbered
) { }
