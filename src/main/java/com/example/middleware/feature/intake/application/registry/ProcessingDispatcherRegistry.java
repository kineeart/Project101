package com.example.middleware.feature.intake.application.registry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.intake.application.port.ProcessingDispatcher;

@Component
public class ProcessingDispatcherRegistry {

    private final Map<String, ProcessingDispatcher> dispatchers =
            new LinkedHashMap<>();

    public ProcessingDispatcherRegistry(
            List<ProcessingDispatcher> dispatchers) {

        for (ProcessingDispatcher dispatcher : dispatchers) {
            this.dispatchers.put(
                    dispatcher.type(),
                    dispatcher
            );
        }
    }

    public ProcessingDispatcher defaultDispatcher() {

        return dispatchers
                .values()
                .stream()
                .findFirst()
                .orElseThrow();
    }

    public ProcessingDispatcher get(String type) {

        ProcessingDispatcher dispatcher =
                dispatchers.get(type);

        if (dispatcher == null) {
            throw new IllegalArgumentException(
                    "Dispatcher not found: " + type
            );
        }

        return dispatcher;
    }
}