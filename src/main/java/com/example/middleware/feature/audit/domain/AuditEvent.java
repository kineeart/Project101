package com.example.middleware.feature.audit.domain;

import java.time.Instant;

public class AuditEvent {

    private final String executionId;

    private final String activity;

    private final String eventType;

    private final String message;

    private final Instant timestamp;

    public AuditEvent(
            String executionId,
            String activity,
            String eventType,
            String message,
            Instant timestamp) {

        this.executionId = executionId;
        this.activity = activity;
        this.eventType = eventType;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getExecutionId() {
        return executionId;
    }

    public String getactivity() {
        return activity;
    }

    public String geteventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
    
}