package com.fever.events_service.domain.ports.in;

public interface SyncEventsUseCase {

    /**
     * Synchronizes events from the provider with the local database.
     * This method is responsible for:
     * 1. Fetching events from the provider
     * 2. Updating or inserting events in the local database
     * 3. Marking obsolete events as unavailable
     * 4. Updating the cache with the latest event information
     *
     * @throws RuntimeException if there's an error during the synchronization process
     */
    void syncEvents();
}
