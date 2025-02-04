package com.fever.events_service.infrastructure.adapters.out.persistence;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.models.Zone;
import com.fever.events_service.infrastructure.adapters.out.persistence.entities.EventEntity;
import com.fever.events_service.infrastructure.adapters.out.persistence.entities.ZoneEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EventMapper {

    public Event toEvent(EventEntity entity) {
        return Event.builder()
                .baseEventId(entity.getBaseEventId())
                .eventId(entity.getEventId())
                .title(entity.getTitle())
                .sellMode(entity.getSellMode())
                .organizerCompanyId(entity.getOrganizerCompanyId())
                .eventStartDate(entity.getStartDate())
                .eventEndDate(entity.getEndDate())
                .sellFrom(entity.getSellFrom())
                .sellTo(entity.getSellTo())
                .soldOut(entity.isSoldOut())
                .zones(entity.getZones().stream()
                        .map(this::toZone)
                        .collect(Collectors.toList()))
                .build();
    }

    public Zone toZone(ZoneEntity entity) {
        return Zone.builder()
                .zoneId(entity.getZoneId())
                .name(entity.getName())
                .minCapacity(entity.getMinCapacity())
                .maxCapacity(entity.getMaxCapacity())
                .minPrice(entity.getMinPrice())
                .maxPrice(entity.getMaxPrice())
                .numbered(entity.isNumbered())
                .build();
    }

    public EventEntity toEventEntity(Event event) {
        EventEntity entity = new EventEntity();
        entity.setBaseEventId(event.getBaseEventId());
        entity.setEventId(event.getEventId());
        entity.setTitle(event.getTitle());
        entity.setSellMode(event.getSellMode());
        entity.setOrganizerCompanyId(event.getOrganizerCompanyId());
        entity.setStartDate(event.getEventStartDate());
        entity.setEndDate(event.getEventEndDate());
        entity.setSellFrom(event.getSellFrom());
        entity.setSellTo(event.getSellTo());
        entity.setSoldOut(event.isSoldOut());
        entity.setZones(event.getZones().stream()
                .map(this::toZoneEntity)
                .collect(Collectors.toList()));
        return entity;
    }

    public ZoneEntity toZoneEntity(Zone zone) {
        ZoneEntity entity = new ZoneEntity();
        entity.setZoneId(zone.getZoneId());
        entity.setName(zone.getName());
        entity.setMinCapacity(zone.getMinCapacity());
        entity.setMaxCapacity(zone.getMaxCapacity());
        entity.setMinPrice(zone.getMinPrice());
        entity.setMaxPrice(zone.getMaxPrice());
        entity.setNumbered(zone.isNumbered());
        return entity;
    }
}
