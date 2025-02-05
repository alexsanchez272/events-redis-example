package com.fever.events_service.infrastructure.adapters.in.rest.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProviderZoneDTO {

    @JacksonXmlProperty(localName = "zone_id")
    private String zoneId;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "capacity")
    private int capacity;

    @JacksonXmlProperty(localName = "price")
    private BigDecimal price;

    @JacksonXmlProperty(localName = "numbered")
    private boolean numbered;
}
