package com.fever.events_service.infrastructure.adapters.in.scheduler;

import com.fever.events_service.domain.ports.in.SyncEventsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventSyncScheduler {

    private final SyncEventsUseCase syncEventsUseCase;

    @Scheduled(cron = "${scheduler.sync.cron}")
    public void performSync() {
        log.info("process=perform_sync, status=init");
        syncEventsUseCase.syncEvents();
        log.info("process=perform_sync, status=completed");
    }
}
