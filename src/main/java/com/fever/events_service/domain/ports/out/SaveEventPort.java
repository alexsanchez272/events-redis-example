package com.fever.events_service.domain.ports.out;

import com.fever.events_service.domain.models.Event;

public interface SaveEventPort {

    void saveEvent (Event event);
}
