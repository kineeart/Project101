package com.example.middleware.feature.orchestration.application.lifecycle;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.orchestration.application.event.ExecutionEventPublisher;
import com.example.middleware.feature.orchestration.domain.Execution;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEvent;
import com.example.middleware.feature.orchestration.domain.event.ExecutionEventType;

@Service
public class DefaultExecutionLifecycleManager
        implements ExecutionLifecycleManager {

    private final ExecutionEventPublisher publisher;

    public DefaultExecutionLifecycleManager(
            ExecutionEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void start(Execution execution) {

        execution.start();

        publisher.publish(
                new ExecutionEvent(
                        execution.getExecutionId(),
                        ExecutionEventType.STARTED,
                        null,
                        null,
                        null,
                        Instant.now()
                )
        );
    }

    @Override
    public void stageFinished(
            Execution execution,
            String stageName,
            StageResult result,
            String detail) {
        execution.stageStarted(stageName);
        execution.stageFinished(
                stageName,
                result,
                detail
        );

        publisher.publish(
                new ExecutionEvent(
                        execution.getExecutionId(),
                        ExecutionEventType.STAGE_FINISHED,
                        stageName,
                        result,
                        detail,
                        Instant.now()
                )
        );
    }

    @Override
    public void complete(Execution execution) {

        execution.complete();

        publisher.publish(
                new ExecutionEvent(
                        execution.getExecutionId(),
                        ExecutionEventType.COMPLETED,
                        null,
                        null,
                        null,
                        Instant.now()
                )
        );
    }

    @Override
    public void fail(
            Execution execution,
            String errorMessage) {

        execution.fail(errorMessage);

        publisher.publish(
                new ExecutionEvent(
                        execution.getExecutionId(),
                        ExecutionEventType.FAILED,
                        execution.getCurrentStage(),
                        null,
                        errorMessage,
                        Instant.now()
                )
        );
    }
}