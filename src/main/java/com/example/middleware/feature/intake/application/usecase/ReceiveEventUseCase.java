package com.example.middleware.feature.intake.application.usecase;

import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.processing.domain.event.RawEvent;

public interface ReceiveEventUseCase {

    EventRecord receive(RawEvent event);

}