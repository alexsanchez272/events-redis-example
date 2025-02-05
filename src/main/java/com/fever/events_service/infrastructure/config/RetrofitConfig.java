package com.fever.events_service.infrastructure.config;

import com.fever.events_service.infrastructure.adapters.out.http.ProviderApi;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jaxb.JaxbConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfig {

    @Value("${events.provider.url}")
    private String providerUrl;

    @Bean
    public Retrofit retrofitClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(providerUrl)
                .client(okHttpClient)
                .addConverterFactory(JaxbConverterFactory.create())
                .build();
    }

    @Bean
    public ProviderApi retrofitProviderApi(Retrofit retrofit) {
        return retrofit.create(ProviderApi.class);
    }
}
