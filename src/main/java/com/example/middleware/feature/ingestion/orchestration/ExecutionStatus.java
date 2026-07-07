package com.example.middleware.feature.orchestration.domain;

public enum ExecutionStatus {
	RECEIVED,
	VALIDATED,
	TRANSFORMED,
	DELIVERED,
	AUDITED,
	FAILED
}
