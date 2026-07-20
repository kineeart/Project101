package com.example.middleware.feature.runtime.application.model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.middleware.feature.runtime.domain.BatchStatus;
import com.example.middleware.feature.runtime.domain.history.BatchHistoryRecord;

public record BatchDetail(

        String batchId,

        BatchStatus status,

        LocalDateTime receivedAt,

        LocalDateTime updatedAt,

        List<BatchHistoryRecord> history

) {
}