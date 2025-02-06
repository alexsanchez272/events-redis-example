package com.fever.events_service.infrastructure.adapters.out.persistence;

import com.fever.events_service.infrastructure.adapters.out.persistence.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String> {

    @Query("SELECT e FROM EventEntity e WHERE e.startDate >= :startsAt AND e.endDate <= :endsAt AND e.available = true")
    List<EventEntity> findEventsBetweenDates(LocalDateTime startsAt, LocalDateTime endsAt);

    @Query("SELECT e.baseEventId FROM EventEntity e WHERE e.available = true")
    Set<String> findActiveEventIds();

    @Modifying
    @Query("UPDATE EventEntity e SET e.available = :status WHERE e.baseEventId IN :ids")
    void updateEventsActiveStatus(@Param("ids") Set<String> ids, @Param("status") boolean status);
}
