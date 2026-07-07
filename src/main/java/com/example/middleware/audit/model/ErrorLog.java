package com.example.middleware.audit.model;

import java.time.LocalDateTime;

public class ErrorLog {

    private String eventId;

    private String errorCode;

    private String errorMessage;

    private String stackTrace;

    private LocalDateTime createdAt =
            LocalDateTime.now();

    public ErrorLog() {
    }

    public ErrorLog(
            String eventId,
            String errorCode,
            String errorMessage,
            String stackTrace) {

        this.eventId = eventId;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    public String getEventId() {
        return eventId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}