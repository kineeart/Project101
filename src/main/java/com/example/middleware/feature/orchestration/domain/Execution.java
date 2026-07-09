package com.example.middleware.feature.orchestration.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.middleware.feature.orchestration.application.StageResult;

public class Execution {

    private String executionId;

    private ExecutionStatus status;

    private List<ExecutionStep> steps = new ArrayList<>();

    private Instant startedAt;

    private Instant finishedAt;

    public Execution() {
    }

    public Execution(String executionId) {
        this.executionId = executionId;
        this.status = ExecutionStatus.PENDING;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public List<ExecutionStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ExecutionStep> steps) {
        this.steps = steps;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }
	public void start() {
    this.status = ExecutionStatus.RUNNING;
    this.startedAt = Instant.now();
}
public void complete() {
    this.status = ExecutionStatus.COMPLETED;
    this.finishedAt = Instant.now();
}
public void fail() {
    this.status = ExecutionStatus.FAILED;
    this.finishedAt = Instant.now();
}
public void stop() {
    this.status = ExecutionStatus.STOPPED;
    this.finishedAt = Instant.now();
}
public void addStep(
        String stageName,
        StageResult result,
        String detail) {

    steps.add(
            new ExecutionStep(
                    stageName,
                    result,
                    detail,
                    Instant.now()
            )
    );
}
}