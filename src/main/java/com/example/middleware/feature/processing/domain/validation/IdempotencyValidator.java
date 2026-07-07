package com.example.middleware.feature.processing.domain.validation;

import com.example.middleware.feature.processing.application.port.IdempotencyPort;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdempotencyValidator implements IdempotencyPort {

    private final ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<>();

    public boolean isNewEvent(String eventId) {
        if (eventId == null) return false;
        return cache.putIfAbsent(eventId, true) == null;
    }
}