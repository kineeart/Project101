package com.example.middleware.feature.intake.domain;

import java.time.LocalDateTime;

public class EventRecord {

    private final String eventId;

    private final String profileId;

    private EventStatus status;

    private String filePath;

    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public EventRecord(
            String eventId,
            String profileId) {

        this.eventId = eventId;
        this.profileId = profileId;
        this.status = EventStatus.RECEIVED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    public void markProcessing() {

        this.status = EventStatus.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }


    public void complete(
            String filePath) {

        this.status = EventStatus.COMPLETED;
        this.filePath = filePath;
        this.updatedAt = LocalDateTime.now();
    }


    public void fail() {

        this.status = EventStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }


    public String getEventId() {
        return eventId;
    }


    public String getProfileId() {
        return profileId;
    }


    public EventStatus getStatus() {
        return status;
    }


    public String getFilePath() {
        return filePath;
    }
}