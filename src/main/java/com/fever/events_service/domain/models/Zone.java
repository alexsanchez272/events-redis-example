package com.fever.events_service.domain.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Zone {
    private String zoneId;
    private String name;
    private int minCapacity;
    private int maxCapacity;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private boolean numbered;
}
