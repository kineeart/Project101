package com.example.middleware.feature.monitoring.application.port;

import java.util.List;

import com.example.middleware.feature.monitoring.domain.ExecutionHistoryRecord;

public interface ExecutionHistoryRepository {

    void save(
            ExecutionHistoryRecord record
    );

    List<ExecutionHistoryRecord> findByExecutionId(
            String executionId
    );
}