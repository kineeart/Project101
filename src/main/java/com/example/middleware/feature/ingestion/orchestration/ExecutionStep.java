package com.example.middleware.feature.orchestration.domain;

import java.time.Instant;

public record ExecutionStep(
	String name,
	ExecutionStatus status,
	String detail,
	Instant occurredAt) {
}
