package com.example.middleware.feature.runtime.application.port;

import java.util.Collection;

import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.batch.Batch;

public interface BatchRepositoryPort {

    void save(BatchRecord batch);

    BatchRecord findById(String batchId);
Collection<BatchRecord> findAll();
}
