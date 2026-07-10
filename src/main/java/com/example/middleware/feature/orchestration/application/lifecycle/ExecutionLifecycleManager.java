package com.example.middleware.feature.orchestration.application.lifecycle;

import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.orchestration.domain.Execution;

public interface ExecutionLifecycleManager {

    void start(Execution execution);

    void stageFinished(
            Execution execution,
            String stageName,
            StageResult result,
            String detail
    );

    void complete(Execution execution);

    void fail(
            Execution execution,
            String errorMessage
    );
}