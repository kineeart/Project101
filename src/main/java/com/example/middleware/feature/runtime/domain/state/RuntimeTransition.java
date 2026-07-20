package com.example.middleware.feature.runtime.domain.state;

import com.example.middleware.feature.runtime.domain.batch.BatchStatus; // SỬA ĐỒNG BỘ Ở ĐÂY

public record RuntimeTransition(
        BatchStatus from,
        BatchStatus to
) {
}
