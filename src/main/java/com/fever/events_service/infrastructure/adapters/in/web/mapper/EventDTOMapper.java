package com.fever.events_service.infrastructure.adapters.in.web.mapper;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.models.Zone;
import com.fever.events_service.infrastructure.adapters.in.web.dto.EventDTO;
import com.fever.events_service.infrastructure.adapters.in.web.dto.ZoneDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventDTOMapper {

    public EventDTO toEventDTO(Event event) {
        List<ZoneDTO> zoneDTOs = event.getZones().stream()
                .map(this::toZoneDTO)
                .collect(Collectors.toList());
        return new EventDTO(
                event.getBaseEventId(),
                event.getEventId(),
                event.getTitle(),
                event.getSellMode(),
                event.getOrganizerCompanyId(),
                event.getStartDate(),
                event.getEndDate(),
                event.getSellFrom(),
                event.getSellTo(),
                event.isSoldOut(),
                zoneDTOs
        );
    }

    private ZoneDTO toZoneDTO(Zone zone) {
        return new ZoneDTO(
                zone.getZoneId(),
                zone.getName(),
                zone.getMinCapacity(),
                zone.getMaxCapacity(),
                zone.getMinPrice(),
                zone.getMaxPrice(),
                zone.isNumbered()
        );
    }

    public List<EventDTO> toEventDTOList(List<Event> events) {
        return events.stream()
                .map(this::toEventDTO)
                .collect(Collectors.toList());
    }
}
