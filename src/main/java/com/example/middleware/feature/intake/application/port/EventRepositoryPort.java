package com.example.middleware.feature.intake.application.port;

import com.example.middleware.feature.intake.domain.EventRecord;

public interface EventRepositoryPort {

    void save(EventRecord event);


    EventRecord findById(String eventId);

}