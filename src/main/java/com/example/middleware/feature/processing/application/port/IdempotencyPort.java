package com.example.middleware.feature.processing.application.port;

public interface IdempotencyPort {

    boolean isNewEvent(String eventId);
}