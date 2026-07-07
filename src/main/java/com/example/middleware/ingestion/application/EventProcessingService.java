package com.example.middleware.ingestion.application;

import com.example.middleware.feature.processing.application.ProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventProcessingService {

    private final ProcessingService processingService;

    public EventProcessingService(ProcessingService processingService) {
        this.processingService = processingService;
    }

    public ResponseEntity<?> process(Map<String, Object> request) {
        return processingService.receive(request);
    }
}