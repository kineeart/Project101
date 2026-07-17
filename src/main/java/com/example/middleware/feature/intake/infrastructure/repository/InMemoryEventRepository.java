package com.example.middleware.feature.intake.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.intake.domain.EventStatus;


@Repository
public class InMemoryEventRepository implements EventRepositoryPort {

    private final Map<String, EventRecord> store = new ConcurrentHashMap<>();

    @Override
    public void save(EventRecord event) {
        store.put(event.getEventId(), event);
    }
 
    @Override
    public List<EventRecord> findByStatus(EventStatus status) {
        return store.values()
                .stream()
                .filter(event -> event.getStatus() == status)
                .toList();
    }

    @Override
    public EventRecord findById(String eventId) {
        return store.get(eventId);
    }

    @Override
    public Optional<EventRecord> claimNext() {
        for (EventRecord event : store.values()) {
            // Chuyển điều kiện check Status lên trước log
            if (event.getStatus() != EventStatus.RECEIVED) {
                continue;
            }

            // Chỉ in log khi sự kiện thực sự được chọn để chuyển sang PROCESSING
            System.out.println("[CLAIM SUCCESS] Event picked for processing: " + event.getEventId());

            event.markProcessing();

            store.put(event.getEventId(), event);

            return Optional.of(event);
        }

        return Optional.empty();
    }
}
