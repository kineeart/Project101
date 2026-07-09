package com.example.middleware.feature.orchestration.application;

import com.example.middleware.feature.ingestion.application.ReceiveEventUseCase;
import com.example.middleware.feature.orchestration.application.port.EventOrchestratorPort;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventOrchestrator implements EventOrchestratorPort {

    private final ReceiveEventUseCase receiveEventUseCase;

    public EventOrchestrator(ReceiveEventUseCase receiveEventUseCase) {
        this.receiveEventUseCase = receiveEventUseCase;
    }
@Override
    public ResponseEntity<?> orchestrate(Map<String, Object> request) {
        return receiveEventUseCase.receive(request);
    }
}