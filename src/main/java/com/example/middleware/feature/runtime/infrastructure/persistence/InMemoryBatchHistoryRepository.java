package com.example.middleware.feature.runtime.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.middleware.feature.runtime.application.port.BatchHistoryRepositoryPort;
import com.example.middleware.feature.runtime.domain.history.BatchHistoryRecord;

@Repository
public class InMemoryBatchHistoryRepository
        implements BatchHistoryRepositoryPort {

    private final Map<String, List<BatchHistoryRecord>> store =
            new ConcurrentHashMap<>();

    @Override
    public void save(BatchHistoryRecord record) {

        store.computeIfAbsent(
                record.getBatchId(),
                id -> new ArrayList<>()
        ).add(record);
    }

    @Override
    public List<BatchHistoryRecord> findByBatchId(
            String batchId) {

        return store.getOrDefault(
                batchId,
                List.of()
        );
    }
}