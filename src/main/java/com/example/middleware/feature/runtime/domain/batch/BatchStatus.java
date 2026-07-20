package com.example.middleware.feature.runtime.domain.batch;

public enum BatchStatus {
    RECEIVED,
    PROCESSING,
    VALIDATED,
    MAPPED,
    BUILT,
    WRITING,
    WRITTEN,
    PARTIAL,
    FAILED
}
