package com.fever.events_service.infrastructure.adapters.in.rest.mapper;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.models.Zone;
import com.fever.events_service.infrastructure.adapters.in.rest.dto.ProviderBaseEventDTO;
import com.fever.events_service.infrastructure.adapters.in.rest.dto.ProviderEventDTO;
import com.fever.events_service.infrastructure.adapters.in.rest.dto.ProviderZoneDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProviderEventMapper {

    public List<Event> mapToEvents(List<ProviderBaseEventDTO> baseEvents) {
        return baseEvents.stream()
                .flatMap(baseEvent -> baseEvent.getEvents().stream()
                        .map(event -> mapToEvent(baseEvent, event)))
                .collect(Collectors.toList());
    }

    private Event mapToEvent(ProviderBaseEventDTO baseEvent, ProviderEventDTO eventDTO) {
        Event event = new Event();
        event.setBaseEventId(baseEvent.getBaseEventId());
        event.setEventId(eventDTO.getEventId());
        event.setTitle(baseEvent.getTitle());
        event.setStartDate(eventDTO.getStartDate());
        event.setEndDate(eventDTO.getEndDate());
        event.setSellFrom(eventDTO.getSellFrom());
        event.setSellTo(eventDTO.getSellTo());
        event.setSoldOut(eventDTO.isSoldOut());
        event.setZones(consolidateZones(eventDTO.getZones()));
        return event;
    }

    private List<Zone> consolidateZones(List<ProviderZoneDTO> providerZones) {
        Map<String, Zone> consolidatedZones = providerZones.stream()
                .collect(Collectors.toMap(
                        ProviderZoneDTO::getZoneId,
                        this::initializeZone,
                        this::mergeZones
                ));

        return List.copyOf(consolidatedZones.values());
    }

    private Zone initializeZone(ProviderZoneDTO zoneDTO) {
        Zone zone = new Zone();
        zone.setZoneId(zoneDTO.getZoneId());
        zone.setName(zoneDTO.getName());
        zone.setMinCapacity(zoneDTO.getCapacity());
        zone.setMaxCapacity(zoneDTO.getCapacity());
        zone.setMinPrice(zoneDTO.getPrice());
        zone.setMaxPrice(zoneDTO.getPrice());
        zone.setNumbered(zoneDTO.isNumbered());
        return zone;
    }

    private Zone mergeZones(Zone existingZone, Zone newZone) {
        existingZone.setMinCapacity(Math.min(existingZone.getMinCapacity(), newZone.getMinCapacity()));
        existingZone.setMaxCapacity(Math.max(existingZone.getMaxCapacity(), newZone.getMaxCapacity()));
        existingZone.setMinPrice(existingZone.getMinPrice().min(newZone.getMinPrice()));
        existingZone.setMaxPrice(existingZone.getMaxPrice().max(newZone.getMaxPrice()));

        return existingZone;
    }
}