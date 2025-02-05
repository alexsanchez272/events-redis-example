package com.fever.events_service.infrastructure.adapter;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.models.Zone;
import com.fever.events_service.infrastructure.adapters.in.rest.dto.ProviderBaseEventDTO;
import com.fever.events_service.infrastructure.adapters.in.rest.dto.ProviderEventDTO;
import com.fever.events_service.infrastructure.adapters.in.rest.dto.ProviderZoneDTO;
import com.fever.events_service.infrastructure.adapters.out.persistence.entities.EventEntity;
import com.fever.events_service.infrastructure.adapters.out.persistence.entities.ZoneEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestDataFactory {

    public static Event createEvent(String baseEventId) {
        switch (baseEventId) {
            case "291":
                return createCamelaEvent();
            case "322":
                return createPantomimaFullEvent();
            case "1591":
                return createLosMorancosEvent();
            default:
                throw new IllegalArgumentException("Unknown base_event_id: " + baseEventId);
        }
    }

    public static Event createEvent(String baseEventId, String title, LocalDateTime startDate, LocalDateTime endDate, Boolean available) {
        return Event.builder()
                .baseEventId(baseEventId)
                .eventId(baseEventId)
                .title(title)
                .sellMode("online")
                .startDate(startDate)
                .endDate(endDate)
                .sellFrom(startDate.minusDays(30))
                .sellTo(endDate.minusHours(1))
                .soldOut(false)
                .available(available)
                .zones(Collections.emptyList())
                .build();
    }

    private static Event createCamelaEvent() {
        return Event.builder()
                .baseEventId("291")
                .eventId("291")
                .title("Camela en concierto")
                .sellMode("online")
                .startDate(LocalDateTime.parse("2021-06-30T21:00:00"))
                .endDate(LocalDateTime.parse("2021-06-30T22:00:00"))
                .sellFrom(LocalDateTime.parse("2020-07-01T00:00:00"))
                .sellTo(LocalDateTime.parse("2021-06-30T20:00:00"))
                .soldOut(false)
                .available(true)
                .zones(Arrays.asList(
                        createZone("40", "Platea", 243, 243, BigDecimal.valueOf(20.00), BigDecimal.valueOf(20.00), true)
                ))
                .build();
    }

    private static Event createPantomimaFullEvent() {
        return Event.builder()
                .baseEventId("322")
                .eventId("1642")
                .title("Pantomima Full")
                .sellMode("online")
                .organizerCompanyId("2")
                .startDate(LocalDateTime.parse("2021-02-10T20:00:00"))
                .endDate(LocalDateTime.parse("2021-02-10T21:30:00"))
                .sellFrom(LocalDateTime.parse("2021-01-01T00:00:00"))
                .sellTo(LocalDateTime.parse("2021-02-09T19:50:00"))
                .soldOut(false)
                .available(true)
                .zones(Arrays.asList(
                        createZone("311", "A42", 2, 2, BigDecimal.valueOf(55.00), BigDecimal.valueOf(55.00), true)
                ))
                .build();
    }

    private static Event createNotAvailableEvent() {
        return Event.builder()
                .baseEventId("322")
                .eventId("1642")
                .title("Sabina")
                .sellMode("online")
                .organizerCompanyId("2")
                .startDate(LocalDateTime.parse("2021-02-10T20:00:00"))
                .endDate(LocalDateTime.parse("2021-02-10T21:30:00"))
                .sellFrom(LocalDateTime.parse("2021-01-01T00:00:00"))
                .sellTo(LocalDateTime.parse("2021-02-09T19:50:00"))
                .soldOut(true)
                .available(false)
                .zones(Arrays.asList(
                        createZone("311", "A42", 2, 2, BigDecimal.valueOf(55.00), BigDecimal.valueOf(55.00), true)
                ))
                .build();
    }

    private static Event createLosMorancosEvent() {
        return Event.builder()
                .baseEventId("1591")
                .eventId("1642")
                .title("Los Morancos")
                .sellMode("online")
                .organizerCompanyId("1")
                .startDate(LocalDateTime.parse("2021-07-31T20:00:00"))
                .endDate(LocalDateTime.parse("2021-07-31T21:00:00"))
                .sellFrom(LocalDateTime.parse("2021-06-26T00:00:00"))
                .sellTo(LocalDateTime.parse("2021-07-31T19:50:00"))
                .soldOut(false)
                .available(true)
                .zones(Arrays.asList(
                        createZone("186", "Amfiteatre", 2, 16, BigDecimal.valueOf(65.00), BigDecimal.valueOf(75.00), true)
                ))
                .build();
    }

    private static Zone createZone(String zoneId, String name, int minCapacity, int maxCapacity, BigDecimal minPrice, BigDecimal maxPrice, boolean numbered) {
        return Zone.builder()
                .zoneId(zoneId)
                .name(name)
                .minCapacity(minCapacity)
                .maxCapacity(maxCapacity)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .numbered(numbered)
                .build();
    }

    public static EventEntity createTestEventEntity(String baseEventId, Boolean available) {
        Event event = createEvent(baseEventId);
        EventEntity eventEntity = new EventEntity();
        eventEntity.setBaseEventId(event.getBaseEventId());
        eventEntity.setEventId(event.getEventId());
        eventEntity.setTitle(event.getTitle());
        eventEntity.setSellMode(event.getSellMode());
        eventEntity.setOrganizerCompanyId(event.getOrganizerCompanyId());
        eventEntity.setStartDate(event.getStartDate());
        eventEntity.setEndDate(event.getEndDate());
        eventEntity.setSellFrom(event.getSellFrom());
        eventEntity.setSellTo(event.getSellTo());
        eventEntity.setSoldOut(event.isSoldOut());
        eventEntity.setAvailable(available);
        eventEntity.setZones(event.getZones().stream().map(TestDataFactory::createZoneEntity).toList());
        return eventEntity;
    }

    private static ZoneEntity createZoneEntity(Zone zone) {
        ZoneEntity zoneEntity = new ZoneEntity();
        zoneEntity.setZoneId(zone.getZoneId());
        zoneEntity.setName(zone.getName());
        zoneEntity.setMinCapacity(zone.getMinCapacity());
        zoneEntity.setMaxCapacity(zone.getMaxCapacity());
        zoneEntity.setMinPrice(zone.getMinPrice());
        zoneEntity.setMaxPrice(zone.getMaxPrice());
        zoneEntity.setNumbered(zone.isNumbered());
        return zoneEntity;
    }

    public static List<Event> createMultipleTestEvents() {
        return Arrays.asList(
                createEvent("291"),
                createEvent("322"),
                createEvent("1591")
        );
    }

    public static List<EventEntity> createMultipleTestEventEntities() {
        return Arrays.asList(
                createTestEventEntity("291", true),
                createTestEventEntity("322", true),
                createTestEventEntity("1591", true)
        );
    }

    public static ProviderBaseEventDTO createProviderBaseEventDTO(String baseEventId) {
        ProviderBaseEventDTO baseEventDTO = new ProviderBaseEventDTO();
        baseEventDTO.setBaseEventId(baseEventId);
        baseEventDTO.setTitle(createEvent(baseEventId).getTitle());
        baseEventDTO.setEvents(Collections.singletonList(createProviderEventDTO(baseEventId)));
        return baseEventDTO;
    }

    public static ProviderEventDTO createProviderEventDTO(String baseEventId) {
        Event event = createEvent(baseEventId);
        ProviderEventDTO eventDTO = new ProviderEventDTO();
        eventDTO.setEventId(event.getEventId());
        eventDTO.setStartDate(event.getStartDate());
        eventDTO.setEndDate(event.getEndDate());
        eventDTO.setSellFrom(event.getSellFrom());
        eventDTO.setSellTo(event.getSellTo());
        eventDTO.setSoldOut(event.isSoldOut());
        eventDTO.setZones(event.getZones().stream().map(TestDataFactory::createProviderZoneDTO).toList());
        return eventDTO;
    }

    private static ProviderZoneDTO createProviderZoneDTO(Zone zone) {
        ProviderZoneDTO zoneDTO = new ProviderZoneDTO();
        zoneDTO.setZoneId(zone.getZoneId());
        zoneDTO.setName(zone.getName());
        zoneDTO.setCapacity(zone.getMinCapacity());
        zoneDTO.setPrice(zone.getMinPrice());
        zoneDTO.setNumbered(zone.isNumbered());
        return zoneDTO;
    }

    public static List<ProviderBaseEventDTO> createMultipleProviderBaseEventDTOs() {
        return Arrays.asList(
                createProviderBaseEventDTO("291"),
                createProviderBaseEventDTO("322"),
                createProviderBaseEventDTO("1591")
        );
    }

    // Special method to create Los Morancos event with duplicate zones
    public static ProviderBaseEventDTO createLosMorancosProviderBaseEventDTO() {
        ProviderBaseEventDTO baseEventDTO = createProviderBaseEventDTO("1591");
        ProviderEventDTO eventDTO = baseEventDTO.getEvents().get(0);

        // Create duplicate zone with different capacity and price
        ProviderZoneDTO zone1 = eventDTO.getZones().get(0);
        ProviderZoneDTO zone2 = new ProviderZoneDTO();
        zone2.setZoneId(zone1.getZoneId());
        zone2.setName(zone1.getName());
        zone2.setCapacity(16);
        zone2.setPrice(BigDecimal.valueOf(75.00));
        zone2.setNumbered(zone1.isNumbered());

        eventDTO.setZones(Arrays.asList(zone1, zone2));
        return baseEventDTO;
    }
}
