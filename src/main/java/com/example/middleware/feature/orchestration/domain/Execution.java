package com.example.middleware.feature.orchestration.domain;

import java.time.Instant;
import java.util.List;

public record Execution(
	String executionId,
	ExecutionStatus status,
	List<ExecutionStep> steps,
	Instant startedAt,
	Instant finishedAt) {
}
