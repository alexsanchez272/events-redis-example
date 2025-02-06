package com.fever.events_service.infrastructure.adapters.out.http.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class ProviderBaseEventDTO {
    @JacksonXmlProperty(isAttribute = true, localName = "base_event_id")
    private String baseEventId;

    @JacksonXmlProperty(isAttribute = true, localName = "sell_mode")
    private String sellMode;

    @JacksonXmlProperty(isAttribute = true, localName = "organizer_company_id")
    private String organizerCompanyId;

    @JacksonXmlProperty(isAttribute = true, localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "event")
    private ProviderEventDTO event;
}
