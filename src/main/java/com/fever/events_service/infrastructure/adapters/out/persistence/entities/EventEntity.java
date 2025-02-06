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
    private String baseEventId;

    private String eventId;
    private String title;
    private String sellMode;
    private String organizerCompanyId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime sellFrom;
    private LocalDateTime sellTo;
    private boolean soldOut;
    @Column(name = "available", nullable = false)
    private boolean available = true;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_base_id")
    private List<ZoneEntity> zones;
}
