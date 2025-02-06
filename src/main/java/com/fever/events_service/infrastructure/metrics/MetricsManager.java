package com.fever.events_service.infrastructure.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.function.ToDoubleFunction;

@Component
public class MetricsManager {

    private final MeterRegistry meterRegistry;

    public MetricsManager(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * Crea y registra un contador con el nombre, descripción y tags indicados.
     */
    public Counter createCounter(String name, String description, String... tags) {
        return Counter.builder(name)
                .description(description)
                .tags(tags)
                .register(meterRegistry);
    }

    /**
     * Crea y registra un timer con el nombre, descripción y tags indicados.
     */
    public Timer createTimer(String name, String description, String... tags) {
        return Timer.builder(name)
                .description(description)
                .tags(tags)
                .register(meterRegistry);
    }

    /**
     * Crea y registra un DistributionSummary con el nombre, descripción y tags indicados.
     */
    public DistributionSummary createSummary(String name, String description, String... tags) {
        return DistributionSummary.builder(name)
                .description(description)
                .tags(tags)
                .register(meterRegistry);
    }

    /**
     * Registra y ejecuta una operación medible con un Timer, devolviendo el resultado de la operación.
     */
    public <T> T recordTimer(String name, Callable<T> operation, String... tags) throws Exception {
        Timer timer = Timer.builder(name)
                .tags(tags)
                .register(meterRegistry);
        return timer.recordCallable(operation);
    }

    /**
     * Registra y ejecuta una operación medible con un Timer.
     */
    public void recordTimer(String name, Runnable operation, String... tags) {
        Timer timer = Timer.builder(name)
                .tags(tags)
                .register(meterRegistry);
        timer.record(operation);
    }

    /**
     * Incrementa un contador registrado previamente o lo crea si no existe.
     */
    public void incrementCounter(String name, String... tags) {
        meterRegistry.counter(name, tags).increment();
    }

    /**
     * Registra un Gauge para medir un valor en tiempo real.
     */
    public <T> void registerGauge(String name, T obj, ToDoubleFunction<T> valueFunction, String... tags) {
        Gauge.builder(name, obj, valueFunction)
                .tags(tags)
                .register(meterRegistry);
    }
}
