package com.fever.events_service.infrastructure.adapter.out.http;

import com.fever.events_service.domain.exceptions.ProviderCommunicationException;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.infrastructure.adapter.TestDataFactory;
import com.fever.events_service.infrastructure.adapters.in.rest.dto.ProviderEventListDTO;
import com.fever.events_service.infrastructure.adapters.in.rest.mapper.ProviderEventMapper;
import com.fever.events_service.infrastructure.adapters.out.http.EventProviderAdapter;
import com.fever.events_service.infrastructure.adapters.out.http.ProviderApi;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventProviderAdapterTest {

    @Mock
    private ProviderApi providerApi;

    @Mock
    private ProviderEventMapper providerEventMapper;

    @Mock
    private Call<ProviderEventListDTO> call;

    private EventProviderAdapter eventProviderAdapter;

    @BeforeEach
    void setUp() {
        eventProviderAdapter = new EventProviderAdapter(providerApi, providerEventMapper);
    }

    @Test
    void shouldFetchEventsSuccessfully() throws IOException {
        ProviderEventListDTO providerEventListDTO = new ProviderEventListDTO();
        providerEventListDTO.setBaseEvents(TestDataFactory.createMultipleProviderBaseEventDTOs());
        Response<ProviderEventListDTO> response = Response.success(providerEventListDTO);

        List<Event> expectedEvents = TestDataFactory.createMultipleTestEvents();

        when(providerApi.fetchEvents()).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(providerEventMapper.mapToEvents(providerEventListDTO.getBaseEvents()))
                .thenReturn(expectedEvents);

        List<Event> actualEvents = eventProviderAdapter.fetchEvents();

        assertEquals(expectedEvents, actualEvents);
        verify(providerApi).fetchEvents();
        verify(call).execute();
        verify(providerEventMapper).mapToEvents(providerEventListDTO.getBaseEvents());
    }

    @Test
    void shouldThrowProviderCommunicationExceptionWhenIOExceptionOccurs() throws IOException {
        when(providerApi.fetchEvents()).thenReturn(call);
        when(call.execute()).thenThrow(new IOException("Network error"));

        assertThrows(ProviderCommunicationException.class, () -> eventProviderAdapter.fetchEvents());
        verify(providerApi).fetchEvents();
        verify(call).execute();
    }

    @Test
    void shouldThrowProviderCommunicationExceptionWhenResponseIsUnsuccessful() throws IOException {
        Response<ProviderEventListDTO> response = Response.error(500, ResponseBody.create(null, ""));
        when(providerApi.fetchEvents()).thenReturn(call);
        when(call.execute()).thenReturn(response);

        assertThrows(ProviderCommunicationException.class, () -> eventProviderAdapter.fetchEvents());
        verify(providerApi).fetchEvents();
        verify(call).execute();
    }
}
