package com.fever.events_service.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    private String zoneId;
    private String name;
    private int minCapacity;
    private int maxCapacity;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private boolean numbered;
}
