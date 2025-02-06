package com.fever.events_service.infrastructure.adapters.out.http.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "eventList")
public class ProviderEventListDTO {
    @JacksonXmlProperty(localName = "output")
    private ProviderOutputDTO output;
}
