package com.example.middleware.feature.runtime.application.port;

import java.util.Collection;
import java.util.List; // Thêm import này
import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus; // Thêm import này

public interface BatchRepositoryPort {
    void save(BatchRecord batch);
    BatchRecord findById(String batchId);
    Collection<BatchRecord> findAll();
    
    List<BatchRecord> findByStatus(BatchStatus status); // Thêm dòng này
}
