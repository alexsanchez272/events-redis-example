package com.fever.events_service.infrastructure.adapter.in.rest;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.models.Zone;
import com.fever.events_service.infrastructure.adapter.TestDataFactory;
import com.fever.events_service.infrastructure.adapters.in.rest.dto.ProviderBaseEventDTO;
import com.fever.events_service.infrastructure.adapters.in.rest.mapper.ProviderEventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderEventMapperTest {

    private ProviderEventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProviderEventMapper();
    }

    @Test
    void mapToEvents_shouldMapAllFieldsCorrectly() {
        List<ProviderBaseEventDTO> providerEvents = TestDataFactory.createMultipleProviderBaseEventDTOs();

        List<Event> mappedEvents = mapper.mapToEvents(providerEvents);

        assertThat(mappedEvents).hasSize(3);

        Event camelaEvent = mappedEvents.get(0);
        assertThat(camelaEvent.getBaseEventId()).isEqualTo("291");
        assertThat(camelaEvent.getTitle()).isEqualTo("Camela en concierto");
        assertThat(camelaEvent.getStartDate()).isEqualTo("2021-06-30T21:00:00");
        assertThat(camelaEvent.getEndDate()).isEqualTo("2021-06-30T22:00:00");
        assertThat(camelaEvent.getSellFrom()).isEqualTo("2020-07-01T00:00:00");
        assertThat(camelaEvent.getSellTo()).isEqualTo("2021-06-30T20:00:00");
        assertThat(camelaEvent.isSoldOut()).isFalse();

        assertThat(camelaEvent.getZones()).hasSize(1);
        Zone platea = camelaEvent.getZones().get(0);
        assertThat(platea.getZoneId()).isEqualTo("40");
        assertThat(platea.getName()).isEqualTo("Platea");
        assertThat(platea.getMinCapacity()).isEqualTo(243);
        assertThat(platea.getMaxCapacity()).isEqualTo(243);
        assertThat(platea.getMinPrice()).isEqualByComparingTo(BigDecimal.valueOf(20.00));
        assertThat(platea.getMaxPrice()).isEqualByComparingTo(BigDecimal.valueOf(20.00));
        assertThat(platea.isNumbered()).isTrue();
    }

    @Test
    void mapToEvents_shouldConsolidateDuplicateZones() {
        ProviderBaseEventDTO losMorancosEvent = TestDataFactory.createLosMorancosProviderBaseEventDTO();

        List<Event> mappedEvents = mapper.mapToEvents(List.of(losMorancosEvent));

        assertThat(mappedEvents).hasSize(1);
        Event event = mappedEvents.get(0);
        assertThat(event.getBaseEventId()).isEqualTo("1591");
        assertThat(event.getTitle()).isEqualTo("Los Morancos");

        assertThat(event.getZones()).hasSize(1);
        Zone consolidatedZone = event.getZones().get(0);
        assertThat(consolidatedZone.getZoneId()).isEqualTo("186");
        assertThat(consolidatedZone.getName()).isEqualTo("Amfiteatre");
        assertThat(consolidatedZone.getMinCapacity()).isEqualTo(2);
        assertThat(consolidatedZone.getMaxCapacity()).isEqualTo(16);
        assertThat(consolidatedZone.getMinPrice()).isEqualByComparingTo(BigDecimal.valueOf(65.00));
        assertThat(consolidatedZone.getMaxPrice()).isEqualByComparingTo(BigDecimal.valueOf(75.00));
        assertThat(consolidatedZone.isNumbered()).isTrue();
    }
}
