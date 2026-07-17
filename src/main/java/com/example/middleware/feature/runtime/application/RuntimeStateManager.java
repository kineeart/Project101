package com.example.middleware.feature.runtime.application;

public interface RuntimeStateManager {

    void enterStage(
            String batchId,
            String stageName
    );

    void complete(
            String batchId,
            String filePath
    );

    void fail(
            String batchId,
            String message
    );
}
