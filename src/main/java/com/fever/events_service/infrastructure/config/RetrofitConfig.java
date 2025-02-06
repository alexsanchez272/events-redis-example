package com.fever.events_service.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fever.events_service.infrastructure.adapters.out.http.ProviderApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfig {

    @Value("${events.provider.url}")
    private String providerUrl;

    @Bean
    public Retrofit retrofitClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        ObjectMapper xmlMapper = new XmlMapper();

        return new Retrofit.Builder()
                .baseUrl(providerUrl)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(xmlMapper))
                .build();
    }

    @Bean
    public ProviderApi providerApi(Retrofit retrofit) {
        return retrofit.create(ProviderApi.class);
    }
}

