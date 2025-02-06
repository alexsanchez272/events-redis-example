package com.fever.events_service.infrastructure.exceptions;

import com.fever.events_service.domain.exceptions.EventsNotFoundException;
import com.fever.events_service.domain.exceptions.InvalidDateRangeException;
import com.fever.events_service.infrastructure.adapters.in.web.dto.ErrorDTO;
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
        ErrorDTO errorDTO = new ErrorDTO("EVENTS_NOT_FOUND", ex.getMessage());
        EventResponseDTO response = new EventResponseDTO(null, errorDTO);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<EventResponseDTO> handleInvalidDateRangeException(InvalidDateRangeException ex) {
        ErrorDTO errorDTO = new ErrorDTO("INVALID_DATE_RANGE", ex.getMessage());
        EventResponseDTO response = new EventResponseDTO(null, errorDTO);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<EventResponseDTO> handleMissingParams(MissingServletRequestParameterException ex) {
        ErrorDTO errorDTO = new ErrorDTO("BAD_REQUEST", ex.getMessage());
        EventResponseDTO response = new EventResponseDTO(null, errorDTO);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EventResponseDTO> handleGenericException(Exception ex) {
        ErrorDTO errorDTO = new ErrorDTO("INTERNAL_ERROR", "An unexpected errorDTO occurred.");
        EventResponseDTO response = new EventResponseDTO(null, errorDTO);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
