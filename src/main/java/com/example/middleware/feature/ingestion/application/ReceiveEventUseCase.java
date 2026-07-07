package com.example.middleware.feature.ingestion.application;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ReceiveEventUseCase {

	ResponseEntity<?> receive(Map<String, Object> request);
}
