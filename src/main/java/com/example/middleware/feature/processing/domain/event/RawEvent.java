package com.example.middleware.feature.processing.domain.event;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RawEvent {

    private String eventId;

// NEW
private String batchId;

private Integer version;

private LocalDateTime generatedAt;

// EXISTING
private String profileId;

private String sourceTable;

private LocalDateTime receivedAt;

private Map<String, Object> payload;

    public RawEvent() {
    }

    public RawEvent(String eventId,
                    String profileId,
                    String sourceTable, String BatchId, Integer version, LocalDateTime generatedAt,
                    Map<String, Object> payload) {
        this.eventId = eventId;
        this.profileId = profileId;
        this.sourceTable = sourceTable;
        this.payload = payload;
        this.batchId = BatchId;
        this.version = version;
        this.generatedAt = generatedAt;
        this.receivedAt = LocalDateTime.now();
    }

    public String getEventId() {
        return eventId;
    }
public String getBatchId() {
        return batchId;
    }
    public Integer getVersion() {
        return version;
    }
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    public String getProfileId() {
        return profileId;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}