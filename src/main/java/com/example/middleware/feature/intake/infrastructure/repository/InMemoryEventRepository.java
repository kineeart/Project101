package com.example.middleware.feature.intake.infrastructure.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;


@Repository
public class InMemoryEventRepository
        implements EventRepositoryPort {


    private final Map<String, EventRecord> store =
            new ConcurrentHashMap<>();


    @Override
    public void save(EventRecord event) {

        store.put(
                event.getEventId(),
                event
        );
    }


    @Override
    public EventRecord findById(
            String eventId) {

        return store.get(eventId);
    }
}