package com.example.middleware.feature.monitoring.domain;

import java.time.Instant;

import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEventType;

public class ExecutionHistoryRecord {

    private final String executionId;

    private final ExecutionEventType eventType;

    private final String stageName;

    private final StageResult result;

    private final String message;

    private final Instant occurredAt;

    public ExecutionHistoryRecord(
            String executionId,
            ExecutionEventType eventType,
            String stageName,
            StageResult result,
            String message,
            Instant occurredAt
    ) {
        this.executionId = executionId;
        this.eventType = eventType;
        this.stageName = stageName;
        this.result = result;
        this.message = message;
        this.occurredAt = occurredAt;
    }

    public String getExecutionId() {
        return executionId;
    }

    public ExecutionEventType getEventType() {
        return eventType;
    }

    public String getStageName() {
        return stageName;
    }

    public StageResult getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}