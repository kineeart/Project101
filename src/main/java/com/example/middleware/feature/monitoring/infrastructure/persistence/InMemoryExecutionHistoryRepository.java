package com.example.middleware.feature.monitoring.infrastructure.persistence;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.example.middleware.feature.monitoring.application.port.ExecutionHistoryRepository;
import com.example.middleware.feature.monitoring.domain.ExecutionHistoryRecord;

@Repository
public class InMemoryExecutionHistoryRepository
        implements ExecutionHistoryRepository {

    private final List<ExecutionHistoryRecord> store =
            new CopyOnWriteArrayList<>();

    @Override
    public void save(
            ExecutionHistoryRecord record
    ) {
        store.add(record);
    }

    @Override
    public List<ExecutionHistoryRecord> findByExecutionId(
            String executionId
    ) {
        return store.stream()
                .filter(r -> r.getExecutionId().equals(executionId))
                .toList();
    }
}