package com.fever.events_service.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "zones")
@Data
public class ZoneEntity {
    @Id
    private String zoneId;
    private String name;
    private int minCapacity;
    private int maxCapacity;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private boolean numbered;
}
