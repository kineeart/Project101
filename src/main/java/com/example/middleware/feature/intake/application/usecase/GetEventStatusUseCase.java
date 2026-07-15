package com.example.middleware.feature.intake.application.usecase;

import com.example.middleware.feature.intake.domain.EventRecord;

public interface GetEventStatusUseCase {

    EventRecord get(String eventId);

}