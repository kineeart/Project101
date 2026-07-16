package com.example.middleware.feature.delivery.domain;

import java.time.LocalDateTime;

public class DeliveryArtifact {

    private final String eventId;

    private final String fileName;

    private ArtifactStatus status;

    private final LocalDateTime createdAt;


    public DeliveryArtifact(
            String eventId,
            String fileName) {

        this.eventId = eventId;
        this.fileName = fileName;
        this.status = ArtifactStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }


    public String getEventId() {
        return eventId;
    }


    public String getFileName() {
        return fileName;
    }


    public ArtifactStatus getStatus() {
        return status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void markPublished() {
        this.status = ArtifactStatus.PUBLISHED;
    }


    public void markFailed() {
        this.status = ArtifactStatus.FAILED;
    }
}