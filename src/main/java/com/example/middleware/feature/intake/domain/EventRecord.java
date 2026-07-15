package com.example.middleware.feature.intake.domain;

import java.time.LocalDateTime;

import com.example.middleware.feature.processing.domain.event.RawEvent;

public class EventRecord {

    private final String eventId;

    private final String profileId;

    private EventStatus status;

    private String filePath;

    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private final RawEvent rawEvent;

    public EventRecord(
        RawEvent rawEvent) {

    this.rawEvent = rawEvent;

    this.eventId = rawEvent.getEventId();

    this.profileId = rawEvent.getProfileId();

    this.status = EventStatus.RECEIVED;

    this.createdAt = LocalDateTime.now();

    this.updatedAt = LocalDateTime.now();
}


    public void markProcessing() {

        this.status = EventStatus.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }

    public RawEvent getRawEvent() {
    return rawEvent;
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