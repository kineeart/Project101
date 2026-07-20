package com.example.middleware.feature.runtime.application.model;

import java.time.LocalDateTime;

public record BatchSummary(
        String batchId,
        // Ép viết cụ thể đường dẫn tuyệt đối của enum để không bao giờ bị lệch gói
        com.example.middleware.feature.runtime.domain.batch.BatchStatus status,
        LocalDateTime updatedAt
) {
}
