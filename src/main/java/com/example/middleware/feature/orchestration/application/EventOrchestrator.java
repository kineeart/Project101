package com.example.middleware.feature.orchestration.application;

import com.example.middleware.feature.ingestion.application.ReceiveEventUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventOrchestrator {

    private final ReceiveEventUseCase receiveEventUseCase;

    public EventOrchestrator(ReceiveEventUseCase receiveEventUseCase) {
        this.receiveEventUseCase = receiveEventUseCase;
    }

    public ResponseEntity<?> orchestrate(Map<String, Object> request) {
        return receiveEventUseCase.receive(request);
    }
}