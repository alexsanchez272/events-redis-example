package com.fever.events_service.infrastructure.adapters.in.web.exceptions;

import com.fever.events_service.domain.exceptions.EventsNotFoundException;
import com.fever.events_service.domain.exceptions.InvalidDateRangeException;
import com.fever.events_service.domain.models.error.Error;
import com.fever.events_service.infrastructure.adapters.in.web.dto.EventResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventsNotFoundException.class)
    public ResponseEntity<EventResponseDTO> handleEventsNotFoundException(EventsNotFoundException ex) {
        Error error = new Error("EVENTS_NOT_FOUND", ex.getMessage());
        EventResponseDTO response = new EventResponseDTO(null, error);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<EventResponseDTO> handleInvalidDateRangeException(InvalidDateRangeException ex) {
        Error error = new Error("INVALID_DATE_RANGE", ex.getMessage());
        EventResponseDTO response = new EventResponseDTO(null, error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<EventResponseDTO> handleMissingParams(MissingServletRequestParameterException ex) {
        Error error = new Error("BAD_REQUEST", ex.getMessage());
        EventResponseDTO response = new EventResponseDTO(null, error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EventResponseDTO> handleGenericException(Exception ex) {
        Error error = new Error("INTERNAL_ERROR", "An unexpected error occurred.");
        EventResponseDTO response = new EventResponseDTO(null, error);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
