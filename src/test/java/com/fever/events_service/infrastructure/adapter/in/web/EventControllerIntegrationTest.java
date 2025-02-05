package com.fever.events_service.infrastructure.adapter.in.web;

import com.fever.events_service.domain.exceptions.EventsNotFoundException;
import com.fever.events_service.domain.exceptions.InvalidDateRangeException;
import com.fever.events_service.infrastructure.adapter.TestDataFactory;
import com.fever.events_service.infrastructure.adapters.out.persistence.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EventControllerIntegrationTest {

    public static final String API_EVENT_SEARCH = "/api/events/search";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    public void setup() {
        eventRepository.deleteAll();
        eventRepository.saveAll(TestDataFactory.createMultipleTestEventEntities());
    }

    @Test
    public void shouldReturnThreeEventsWhenRangeIsValid() throws Exception {
        String startDate = "2021-01-01T00:00:00";
        String endDate   = "2022-01-01T00:00:00";

        mockMvc.perform(get(API_EVENT_SEARCH)
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.events", hasSize(3)))
                .andExpect(jsonPath("$.data.events[0].baseEventId", notNullValue()));
    }

    @Test
    public void shouldReturnBadRequestWhenStartDateIsAfterEndDate() throws Exception {
        String startDate = "2022-01-01T00:00:00";
        String endDate   = "2021-01-01T00:00:00";

        mockMvc.perform(get(API_EVENT_SEARCH)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(
                        InvalidDateRangeException.class,
                        result.getResolvedException(),
                        "Se esperaba una InvalidDateRangeException")
                );
    }

    @Test
    public void shouldReturnBadRequestWhenMissingParameters() throws Exception {
        String startDate = "2021-01-01T00:00:00";

        mockMvc.perform(get(API_EVENT_SEARCH)
                        .param("startDate", startDate))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.error.message").isNotEmpty())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    public void shouldReturnNotFoundWhenNoEventsFoundForRange() throws Exception {
        eventRepository.deleteAll();

        String startDate = "2020-01-01T00:00:00";
        String endDate   = "2020-12-01T00:00:00";

        mockMvc.perform(get(API_EVENT_SEARCH)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(
                        EventsNotFoundException.class,
                        result.getResolvedException(),
                        "Se esperaba una EventsNotFoundException"
                ));
    }
}
