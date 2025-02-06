package com.fever.events_service.application.usecases;

import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.in.SyncEventsUseCase;
import com.fever.events_service.domain.ports.out.EventCachePort;
import com.fever.events_service.domain.ports.out.EventPersistencePort;
import com.fever.events_service.domain.ports.out.EventProviderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncEventsUseCaseImpl implements SyncEventsUseCase {

    private final EventProviderPort eventProviderPort;
    private final EventPersistencePort eventPersistencePort;
    private final EventCachePort eventCachePort;

    @Override
    @Transactional
    public void syncEvents() {
        log.info("process=sync_events, status=start");
        try {
            List<Event> providerEvents = eventProviderPort.fetchEvents();
            if (providerEvents.isEmpty()) {
                log.info("process=sync_events, status=no_events_to_sync");
                return;
            }

            Set<String> activeEventIds = eventPersistencePort.fetchActiveEventIds();
            log.info("process=sync_events, active_event_ids_count={}", activeEventIds.size());

            Set<String> providerEventIds = providerEvents.stream()
                    .map(Event::getBaseEventId)
                    .collect(Collectors.toSet());

            Set<String> obsoleteIds = activeEventIds.stream()
                    .filter(id -> !providerEventIds.contains(id))
                    .collect(Collectors.toSet());
            log.info("process=sync_events, obsolete_ids_count={}", obsoleteIds.size());

            if (!obsoleteIds.isEmpty()) {
                eventPersistencePort.markEventsAsInactiveByIds(obsoleteIds);
                log.info("process=sync_events, marked_inactive_count={}", obsoleteIds.size());
            }

            eventPersistencePort.saveOrUpdateEvents(providerEvents);
            log.info("process=sync_events, saved_or_updated_count={}", providerEvents.size());

            updateCache(providerEvents);

            log.info("process=sync_events, status=success, eventsCount={}, obsoleteCount={}",
                    providerEvents.size(), obsoleteIds.size());
        } catch (Exception e) {
            log.error("process=sync_events, status=error, message={}", e.getMessage(), e);
        }
    }

    private void updateCache(List<Event> events) {
        if (events.isEmpty()) {
            return;
        }
        LocalDateTime earliestDate = events.stream()
                .map(Event::getStartDate)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        LocalDateTime latestDate = events.stream()
                .map(Event::getEndDate)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        eventCachePort.invalidateCache(earliestDate, latestDate);
    }
}
