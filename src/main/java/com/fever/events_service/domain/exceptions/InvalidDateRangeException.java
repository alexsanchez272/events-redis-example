package com.fever.events_service.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateRangeException extends EventServiceException {
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
