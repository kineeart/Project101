package com.example.middleware.feature.runtime.application.model;

import java.time.LocalDateTime;
import java.util.List;
import com.example.middleware.feature.runtime.domain.history.BatchHistoryRecord;

public record BatchDetail(
        String batchId,
        // Ép viết cụ thể đường dẫn tuyệt đối của enum để không bao giờ bị lệch gói
        com.example.middleware.feature.runtime.domain.batch.BatchStatus status,
        LocalDateTime receivedAt,
        LocalDateTime updatedAt,
        List<BatchHistoryRecord> history
) {
}
