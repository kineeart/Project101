package com.example.middleware.feature.runtime.application.model;

import java.time.LocalDateTime;
import com.example.middleware.feature.runtime.domain.BatchStatus;

public record BatchSummary(
        String batchId,
        BatchStatus status,
        LocalDateTime updatedAt
) {
}
