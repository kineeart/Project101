package com.example.middleware.feature.orchestration.domain;

import com.example.middleware.feature.orchestration.application.StageResult;

import java.time.Instant;

public record ExecutionStep(

        String name,

        StageResult result,

        String detail,

        Instant occurredAt

) {
}