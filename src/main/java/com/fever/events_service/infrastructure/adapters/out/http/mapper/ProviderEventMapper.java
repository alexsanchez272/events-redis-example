package com.fever.events_service.infrastructure.adapters.out.http.mapper;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.models.Zone;
import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderBaseEventDTO;
import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderEventDTO;
import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderZoneDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProviderEventMapper {

    /**
     * Mapea una lista de ProviderBaseEventDTO a una lista de Event.
     * Ahora que cada base event contiene un único ProviderEventDTO, se procesa
     * cada elemento de la lista llamando a mapToEvent sobre baseEvent.getEvent().
     */
    public List<Event> mapToEvents(List<ProviderBaseEventDTO> baseEvents) {
        return baseEvents.stream()
                .map(baseEvent -> mapToEvent(baseEvent, baseEvent.getEvent()))
                .collect(Collectors.toList());
    }

    /**
     * Mapea un ProviderBaseEventDTO y su ProviderEventDTO asociado a un objeto Event.
     */
    private Event mapToEvent(ProviderBaseEventDTO baseEvent, ProviderEventDTO eventDTO) {
        Event event = new Event();
        event.setBaseEventId(baseEvent.getBaseEventId());
        event.setEventId(eventDTO.getEventId());
        event.setTitle(baseEvent.getTitle());
        event.setSellMode(baseEvent.getSellMode());
        event.setOrganizerCompanyId(baseEvent.getOrganizerCompanyId());
        event.setStartDate(eventDTO.getStartDate());
        event.setEndDate(eventDTO.getEndDate());
        event.setSellFrom(eventDTO.getSellFrom());
        event.setSellTo(eventDTO.getSellTo());
        event.setSoldOut(eventDTO.isSoldOut());
        event.setZones(consolidateZones(eventDTO.getZones()));
        return event;
    }

    /**
     * Consolida la lista de ProviderZoneDTO en una lista de Zone,
     * combinando aquellas con el mismo zoneId.
     */
    private List<Zone> consolidateZones(List<ProviderZoneDTO> providerZones) {
        Map<String, Zone> consolidatedZones = providerZones.stream()
                .collect(Collectors.toMap(
                        ProviderZoneDTO::getZoneId,
                        this::initializeZone,
                        this::mergeZones
                ));

        return List.copyOf(consolidatedZones.values());
    }

    /**
     * Inicializa un objeto Zone a partir de un ProviderZoneDTO.
     */
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

    /**
     * Fusiona dos objetos Zone, combinando capacidades y precios.
     */
    private Zone mergeZones(Zone existingZone, Zone newZone) {
        existingZone.setMinCapacity(Math.min(existingZone.getMinCapacity(), newZone.getMinCapacity()));
        existingZone.setMaxCapacity(Math.max(existingZone.getMaxCapacity(), newZone.getMaxCapacity()));
        existingZone.setMinPrice(existingZone.getMinPrice().min(newZone.getMinPrice()));
        existingZone.setMaxPrice(existingZone.getMaxPrice().max(newZone.getMaxPrice()));
        return existingZone;
    }
}
