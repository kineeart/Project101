package com.example.middleware.feature.runtime.infrastructure.persistence;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.batch.Batch;

@Repository
public class InMemoryBatchRepository
        implements BatchRepositoryPort {

    private final Map<String, BatchRecord> store =
            new ConcurrentHashMap<>();
    @Override
    public void save(BatchRecord batch) {
        store.put(batch.getBatchId(), batch);
    }

    @Override
    public BatchRecord findById(String batchId) {
        return store.get(batchId);
    }
    @Override
public Collection<BatchRecord> findAll() {
    return store.values();
}
}