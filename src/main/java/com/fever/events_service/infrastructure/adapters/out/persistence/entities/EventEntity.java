package com.fever.events_service.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
public class EventEntity {
    @Id
    private String baseEventId;  // Primary identifier

    private String eventId;      // Secondary identifier
    private String title;
    private String sellMode;
    private String organizerCompanyId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime sellFrom;
    private LocalDateTime sellTo;
    private boolean soldOut;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_base_id")
    private List<ZoneEntity> zones;
}
