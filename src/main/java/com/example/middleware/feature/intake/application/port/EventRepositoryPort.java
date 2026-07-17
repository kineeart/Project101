package com.example.middleware.feature.intake.application.port;

import java.util.List;

import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.intake.domain.EventStatus;

public interface EventRepositoryPort {

    void save(EventRecord event);

    EventRecord findById(String eventId);

    List<EventRecord> findAll();

    List<EventRecord> findByStatus(EventStatus status);

}