package com.example.middleware.feature.runtime.infrastructure.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.domain.batch.Batch;

@Repository
public class InMemoryBatchRepository
        implements BatchRepositoryPort {

    private final Map<String, Batch> store =
            new ConcurrentHashMap<>();

    @Override
    public void save(Batch batch) {
        store.put(batch.getBatchId(), batch);
    }

    @Override
    public Batch findById(String batchId) {
        return store.get(batchId);
    }
}