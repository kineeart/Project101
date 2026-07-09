package com.example.middleware.feature.orchestration.application.port;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface EventOrchestratorPort {

    ResponseEntity<?> orchestrate(Map<String, Object> request);

}