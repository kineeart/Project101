package com.example.middleware.feature.intake.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.intake.domain.EventStatus;


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
public List<EventRecord> findByStatus(EventStatus status) {

    return store.values()
            .stream()
            .filter(event -> event.getStatus() == status)
            .toList();

}

    @Override
    public EventRecord findById(
            String eventId) {

        return store.get(eventId);
    }
}