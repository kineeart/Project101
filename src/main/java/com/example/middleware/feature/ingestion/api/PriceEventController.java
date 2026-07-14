package com.example.middleware.feature.ingestion.api;

import com.example.middleware.feature.orchestration.application.EventOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PriceEventController {

    private final EventOrchestrator orchestrator;

    public PriceEventController(EventOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/events")
    public ResponseEntity<?> receive(
            @RequestBody Map<String, Object> request) {

        return orchestrator.orchestrate(request);
    }
}