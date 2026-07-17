package com.example.middleware.feature.runtime.application.port;

import com.example.middleware.feature.runtime.domain.batch.Batch;

public interface BatchRepositoryPort {

    void save(Batch batch);

    Batch findById(String batchId);

}
