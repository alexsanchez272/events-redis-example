package com.fever.events_service.domain.exceptions;

public abstract class EventServiceException extends RuntimeException {
    public EventServiceException(String message) {
        super(message);
    }

    public EventServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
