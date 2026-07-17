package com.example.middleware.feature.intake.domain;

import java.time.LocalDateTime;
import com.example.middleware.feature.processing.domain.event.RawEvent;

public class EventRecord {

    private final String eventId;
    private final String profileId;
    private EventStatus status;
    private String filePath;
    private String errorMessage; // Thêm trường lưu vết message lỗi
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final RawEvent rawEvent;

    public EventRecord(RawEvent rawEvent) {
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

    // Cơ chế Dynamic Stage Mapping dựa trên kiến trúc của Architect
    public void enterStage(String stageName) {
        switch (stageName) {
            case "Validation" -> this.status = EventStatus.VALIDATING;
            case "Mapping" -> this.status = EventStatus.MAPPING;
            case "Delivery" -> this.status = EventStatus.BUILDING;
            case "Writing" -> this.status = EventStatus.WRITING;
            // Dễ dàng bổ sung các trường hợp dynamic khác tại đây (Scheduler, Retry, Lease...)
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void markWritten(String filePath) {
        this.status = EventStatus.WRITTEN;
        this.filePath = filePath;
        this.updatedAt = LocalDateTime.now();
    }

    public void markPartial(String filePath) {
        this.status = EventStatus.PARTIAL;
        this.filePath = filePath;
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed(String message) {
        this.status = EventStatus.FAILED;
        this.errorMessage = message;
        this.updatedAt = LocalDateTime.now();
    }

    // --- Getters ---
    public RawEvent getRawEvent() { return rawEvent; }
    public String getEventId() { return eventId; }
    public String getProfileId() { return profileId; }
    public EventStatus getStatus() { return status; }
    public String getFilePath() { return filePath; }
    public String getErrorMessage() { return errorMessage; }
}
