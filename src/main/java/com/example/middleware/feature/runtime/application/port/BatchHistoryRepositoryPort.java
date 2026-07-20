package com.example.middleware.feature.runtime.application.port;

import java.util.Collection;
import java.util.List;

import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.history.BatchHistoryRecord;

public interface BatchHistoryRepositoryPort {

    void save(BatchHistoryRecord record);

    List<BatchHistoryRecord> findByBatchId(
            String batchId
    );
}