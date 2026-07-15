package com.example.middleware.feature.intake.application.usecase;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;

@Service
public class DefaultGetEventStatusUseCase
        implements GetEventStatusUseCase {

    private final EventRepositoryPort repository;

    public DefaultGetEventStatusUseCase(
            EventRepositoryPort repository) {

        this.repository = repository;
    }

    @Override
    public EventRecord get(String eventId) {

        EventRecord record =
                repository.findById(eventId);

        if (record == null) {
            throw new IllegalArgumentException(
                    "Event not found: " + eventId);
        }

        return record;
    }
}
