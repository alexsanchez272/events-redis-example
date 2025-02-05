package com.fever.events_service.application;

import com.fever.events_service.application.services.EventService;
import com.fever.events_service.application.services.EventServiceImpl;
import com.fever.events_service.domain.exceptions.EventsNotFoundException;
import com.fever.events_service.domain.exceptions.InvalidDateRangeException;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.domain.ports.in.SearchEventsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.fever.events_service.infrastructure.adapter.TestDataFactory.createEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    @Mock
    private SearchEventsUseCase searchEventsUseCase;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventService = new EventServiceImpl(searchEventsUseCase);
    }

    @Test
    void shouldReturnEventsWhenSearchEventsUseCaseReturnsData() {
        // Definimos un rango de fechas que incluya los eventos reales.
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 31, 23, 59);

        List<Event> expectedEvents = Arrays.asList(
                createEvent("291"),
                createEvent("322"),
                createEvent("1591")
        );

        when(searchEventsUseCase.searchEvents(startDate, endDate)).thenReturn(expectedEvents);

        List<Event> result = eventService.searchEvents(startDate, endDate);
        assertEquals(expectedEvents, result);
        verify(searchEventsUseCase).searchEvents(startDate, endDate);
    }

    @Test
    void shouldThrowInvalidDateRangeExceptionWhenStartDateIsAfterEndDate() {
        LocalDateTime startDate = LocalDateTime.of(2021, 12, 31, 23, 59);
        LocalDateTime endDate = LocalDateTime.of(2021, 1, 1, 0, 0);

        assertThrows(InvalidDateRangeException.class, () -> eventService.searchEvents(startDate, endDate));
        verifyNoInteractions(searchEventsUseCase);
    }

    @Test
    void shouldThrowEventsNotFoundExceptionWhenNoEventsFound() {
        LocalDateTime startDate = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);

        when(searchEventsUseCase.searchEvents(startDate, endDate)).thenReturn(Collections.emptyList());

        assertThrows(EventsNotFoundException.class, () -> eventService.searchEvents(startDate, endDate));
        verify(searchEventsUseCase).searchEvents(startDate, endDate);
    }
}
