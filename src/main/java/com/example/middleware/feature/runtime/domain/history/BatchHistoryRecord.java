package com.example.middleware.feature.runtime.domain.history;

import java.time.LocalDateTime;

import com.example.middleware.feature.runtime.domain.BatchStatus;

public class BatchHistoryRecord {

    private final String batchId;

    private final BatchStatus status;

    private final LocalDateTime occurredAt;

    private final String message;

    public BatchHistoryRecord(
            String batchId,
            BatchStatus status,
            String message) {

        this.batchId = batchId;
        this.status = status;
        this.message = message;
        this.occurredAt = LocalDateTime.now();
    }

    public String getBatchId() {
        return batchId;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public String getMessage() {
        return message;
    }
}