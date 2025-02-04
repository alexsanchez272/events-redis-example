package com.fever.events_service.domain.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Event {
    private String baseEventId;
    private String eventId;
    private String title;
    private String sellMode;
    private String organizerCompanyId;
    private LocalDateTime eventStartDate;
    private LocalDateTime eventEndDate;
    private LocalDateTime sellFrom;
    private LocalDateTime sellTo;
    private boolean soldOut;
    private List<Zone> zones;
}
