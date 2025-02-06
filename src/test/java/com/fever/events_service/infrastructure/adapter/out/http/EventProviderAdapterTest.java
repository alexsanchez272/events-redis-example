package com.fever.events_service.infrastructure.adapter.out.http;

import com.fever.events_service.domain.exceptions.ProviderCommunicationException;
import com.fever.events_service.domain.models.Event;
import com.fever.events_service.infrastructure.adapter.TestDataFactory;
import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderBaseEventDTO;
import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderEventListDTO;
import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderOutputDTO;
import com.fever.events_service.infrastructure.adapters.out.http.mapper.ProviderEventMapper;
import com.fever.events_service.infrastructure.adapters.out.http.ProviderApi;
import com.fever.events_service.infrastructure.adapters.out.http.EventProviderAdapter;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    private MeterRegistry meterRegistry;

    @Mock
    private MeterRegistry.Config meterRegistryConfig;

    @Mock
    private Call<ProviderEventListDTO> call;

    private EventProviderAdapter eventProviderAdapter;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a real SimpleMeterRegistry instead of mocking
        meterRegistry = new SimpleMeterRegistry(SimpleConfig.DEFAULT, Clock.SYSTEM);

        eventProviderAdapter = new EventProviderAdapter(providerApi, providerEventMapper, meterRegistry);
    }

    @Test
    void shouldFetchEventsSuccessfully() throws IOException {
        ProviderEventListDTO providerEventListDTO = new ProviderEventListDTO();
        ProviderOutputDTO output = new ProviderOutputDTO();
        output.setBaseEvents(TestDataFactory.createMultipleProviderBaseEventDTOs());
        providerEventListDTO.setOutput(output);

        Response<ProviderEventListDTO> response = Response.success(providerEventListDTO);
        List<Event> expectedEvents = TestDataFactory.createMultipleTestEvents();

        when(providerApi.fetchEvents()).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(providerEventMapper.mapToEvents(providerEventListDTO.getOutput().getBaseEvents()))
                .thenReturn(expectedEvents);

        List<Event> actualEvents = eventProviderAdapter.fetchEvents();

        assertEquals(expectedEvents, actualEvents);
        verify(providerApi).fetchEvents();
        verify(call).execute();
        verify(providerEventMapper).mapToEvents(providerEventListDTO.getOutput().getBaseEvents());
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
        Response<ProviderEventListDTO> response = Response.error(500,
                ResponseBody.create(MediaType.get("application/xml"), ""));
        when(providerApi.fetchEvents()).thenReturn(call);
        when(call.execute()).thenReturn(response);

        assertThrows(ProviderCommunicationException.class, () -> eventProviderAdapter.fetchEvents());
        verify(providerApi).fetchEvents();
        verify(call).execute();
    }

    /**
     * Clase auxiliar para simular el DTO de salida.
     * Si la clase OutputDTO se hace pública en el paquete correspondiente,
     * se podría usar directamente esa clase.
     */
    public static class OutputDTO {
        private List<ProviderBaseEventDTO> baseEvents;

        public OutputDTO(List<ProviderBaseEventDTO> baseEvents) {
            this.baseEvents = baseEvents;
        }

        public List<ProviderBaseEventDTO> getBaseEvents() {
            return baseEvents;
        }

        public void setBaseEvents(List<ProviderBaseEventDTO> baseEvents) {
            this.baseEvents = baseEvents;
        }
    }
}
