package com.fever.events_service.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventsNotFoundException extends EventServiceException {

    public EventsNotFoundException(String message) {
        super(message);
    }
}
