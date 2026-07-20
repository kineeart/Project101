package com.example.middleware.feature.runtime.application.usecase;

import com.example.middleware.feature.runtime.domain.BatchRecord;

public interface ClaimBatchUseCase {
    BatchRecord claim(String batchId);
}
