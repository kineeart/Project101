package com.example.middleware.core.dlq;

import java.time.LocalDateTime;
import java.util.Map;

public class DeadLetterEvent {

    private String eventId;
    private String reason;
    private Map<String, Object> payload;
    private int retryCount;
    private LocalDateTime createdAt = LocalDateTime.now();

    public DeadLetterEvent(String eventId,
                           String reason,
                           Map<String, Object> payload,
                           int retryCount) {
        this.eventId = eventId;
        this.reason = reason;
        this.payload = payload;
        this.retryCount = retryCount;
    }

    public String getEventId() { return eventId; }
}