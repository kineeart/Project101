package com.example.middleware.feature.intake.application.dispatcher;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.ingestion.application.ProcessEventUseCase;
import com.example.middleware.feature.intake.application.port.ProcessingDispatcher;

@Service
public class SyncProcessingDispatcher
        implements ProcessingDispatcher {

    private final ProcessEventUseCase processEventUseCase;

    public SyncProcessingDispatcher(
            ProcessEventUseCase processEventUseCase) {

        this.processEventUseCase = processEventUseCase;
    }

    @Override
    public void dispatch(String eventId) {

        processEventUseCase.process(eventId);

    }
    @Override
public String type() {
    return "SYNC";
}
}
