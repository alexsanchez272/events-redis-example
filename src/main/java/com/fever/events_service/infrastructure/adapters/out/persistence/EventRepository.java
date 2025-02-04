package com.fever.events_service.infrastructure.adapters.out.persistence;

import com.fever.events_service.infrastructure.adapters.out.persistence.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String> {

    @Query("SELECT e FROM EventEntity e WHERE e.startDate >= :startsAt AND e.endDate <= :endsAt")
    List<EventEntity> findEventsBetweenDates(LocalDateTime startsAt, LocalDateTime endsAt);
}
