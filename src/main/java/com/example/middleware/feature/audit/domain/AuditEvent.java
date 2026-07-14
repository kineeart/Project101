package com.example.middleware.feature.audit.domain;

import java.time.Instant;

public class AuditEvent {

    private final String executionId;

    private final String activity;

    private final String outcome;

    private final String message;

    private final Instant timestamp;

    public AuditEvent(
            String executionId,
            String activity,
            String outcome,
            String message,
            Instant timestamp) {

        this.executionId = executionId;
        this.activity = activity;
        this.outcome = outcome;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getExecutionId() {
        return executionId;
    }

    public String getactivity() {
        return activity;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
    
}