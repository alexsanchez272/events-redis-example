package com.fever.events_service.infrastructure.adapters.out.http;

import com.fever.events_service.infrastructure.adapters.out.http.dto.ProviderEventListDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProviderApi {
    @GET("api/events/")
    Call<ProviderEventListDTO> fetchEvents();
}
