package com.fever.events_service.infrastructure.adapters.in.rest.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProviderBaseEventDTO {

    @JacksonXmlProperty(localName = "base_event_id")
    private String baseEventId;

    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "event")
    private List<ProviderEventDTO> events;
}
