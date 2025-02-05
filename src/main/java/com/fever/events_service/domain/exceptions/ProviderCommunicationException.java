package com.fever.events_service.domain.exceptions;

public class ProviderCommunicationException extends RuntimeException {

    public ProviderCommunicationException(String message) {
        super(message);
    }

    public ProviderCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
