package com.example.middleware.feature.runtime.domain.batch;

public interface BatchProcessor {

    void process(
            BatchEnvelope batch
    );

}
