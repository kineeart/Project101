package com.example.middleware.feature.orchestration.domain.event;

import java.time.Instant;

import com.example.middleware.feature.orchestration.application.StageResult;

public record ExecutionEvent(

        String executionId,

        ExecutionEventType type,

        String stageName,

        StageResult stageResult,

        String message,

        Instant occurredAt

) {
}