package com.fever.events_service.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;

public record ZoneDTO(
        String zoneId,
        String name,
        int minCapacity,
        int maxCapacity,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        boolean numbered
) { }
