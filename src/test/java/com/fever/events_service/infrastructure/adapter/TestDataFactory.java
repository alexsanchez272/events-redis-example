package com.fever.events_service.infrastructure.adapter;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.models.Zone;
import com.fever.events_service.infrastructure.adapters.out.persistence.entities.EventEntity;
import com.fever.events_service.infrastructure.adapters.out.persistence.entities.ZoneEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestDataFactory {

    public static Event createTestEvent(String baseEventId) {
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

    private static Event createCamelaEvent() {
        return Event.builder()
                .baseEventId("291")
                .eventId("291")
                .title("Camela en concierto")
                .sellMode("online")
                .eventStartDate(LocalDateTime.parse("2021-06-30T21:00:00"))
                .eventEndDate(LocalDateTime.parse("2021-06-30T22:00:00"))
                .sellFrom(LocalDateTime.parse("2020-07-01T00:00:00"))
                .sellTo(LocalDateTime.parse("2021-06-30T20:00:00"))
                .soldOut(false)
                .zones(Arrays.asList(
                        createZone("40", "Platea", 243, 243, BigDecimal.valueOf(20.00), BigDecimal.valueOf(20.00), true),
                        createZone("38", "Grada 2", 100, 100, BigDecimal.valueOf(15.00), BigDecimal.valueOf(15.00), false),
                        createZone("30", "A28", 90, 90, BigDecimal.valueOf(30.00), BigDecimal.valueOf(30.00), true)
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
                .eventStartDate(LocalDateTime.parse("2021-02-10T20:00:00"))
                .eventEndDate(LocalDateTime.parse("2021-02-10T21:30:00"))
                .sellFrom(LocalDateTime.parse("2021-01-01T00:00:00"))
                .sellTo(LocalDateTime.parse("2021-02-09T19:50:00"))
                .soldOut(false)
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
                .eventStartDate(LocalDateTime.parse("2021-07-31T20:00:00"))
                .eventEndDate(LocalDateTime.parse("2021-07-31T21:00:00"))
                .sellFrom(LocalDateTime.parse("2021-06-26T00:00:00"))
                .sellTo(LocalDateTime.parse("2021-07-31T19:50:00"))
                .soldOut(false)
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

    public static EventEntity createTestEventEntity(String baseEventId) {
        Event event = createTestEvent(baseEventId);
        EventEntity eventEntity = new EventEntity();
        eventEntity.setBaseEventId(event.getBaseEventId());
        eventEntity.setEventId(event.getEventId());
        eventEntity.setTitle(event.getTitle());
        eventEntity.setSellMode(event.getSellMode());
        eventEntity.setOrganizerCompanyId(event.getOrganizerCompanyId());
        eventEntity.setStartDate(event.getEventStartDate());
        eventEntity.setEndDate(event.getEventEndDate());
        eventEntity.setSellFrom(event.getSellFrom());
        eventEntity.setSellTo(event.getSellTo());
        eventEntity.setSoldOut(event.isSoldOut());
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
                createTestEvent("291"),
                createTestEvent("322"),
                createTestEvent("1591")
        );
    }

    public static List<EventEntity> createMultipleTestEventEntities() {
        return Arrays.asList(
                createTestEventEntity("291"),
                createTestEventEntity("322"),
                createTestEventEntity("1591")
        );
    }
}
