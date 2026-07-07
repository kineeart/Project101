package com.example.middleware.audit.model;

import com.example.middleware.shared.enums.PipelineStatus;

import java.time.LocalDateTime;

public class ProcessingLog {

    private String eventId;

    private PipelineStatus status;

    private String message;

    private String filePath;

    private LocalDateTime createdAt;

    public ProcessingLog() {
        this.createdAt = LocalDateTime.now();
    }

    public ProcessingLog(
            String eventId,
            PipelineStatus status,
            String message,
            String filePath) {

        this.eventId = eventId;
        this.status = status;
        this.message = message;
        this.filePath = filePath;
        this.createdAt = LocalDateTime.now();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public PipelineStatus getStatus() {
        return status;
    }

    public void setStatus(PipelineStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getFilePath() {
        return filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}