package com.fever.events_service.infrastructure.adapters.in.rest.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProviderEventDTO {

    @JacksonXmlProperty(localName = "event_id")
    private String eventId;

    @JacksonXmlProperty(localName = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @JacksonXmlProperty(localName = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @JacksonXmlProperty(localName = "sell_from")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sellFrom;

    @JacksonXmlProperty(localName = "sell_to")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sellTo;

    @JacksonXmlProperty(localName = "sold_out")
    private boolean soldOut;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "zone")
    private List<ProviderZoneDTO> zones;
}
