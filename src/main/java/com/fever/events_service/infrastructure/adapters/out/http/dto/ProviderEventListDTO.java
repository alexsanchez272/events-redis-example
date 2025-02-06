package com.fever.events_service.infrastructure.adapters.out.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "eventList")
public class ProviderEventListDTO {
    @JacksonXmlProperty(localName = "version", isAttribute = true)
    private String version;
    @JacksonXmlProperty(localName = "output")
    private ProviderOutputDTO output;
}
