package com.example.middleware.feature.processing.domain.exception;

public class DuplicateEventException extends RuntimeException {

    public DuplicateEventException(String eventId) {
        super("Duplicate eventId: " + eventId);
    }
}