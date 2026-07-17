package com.example.middleware.feature.runtime.domain.batch;

public enum BatchStatus {

    RECEIVED,

    PROCESSING,

    WRITING,

    PENDING_WRITE,

    WRITTEN,

    PARTIAL,

    FAILED

}
