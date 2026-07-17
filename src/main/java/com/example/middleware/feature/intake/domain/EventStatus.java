package com.example.middleware.feature.intake.domain;

public enum EventStatus {

    RECEIVED,

    PROCESSING,

    VALIDATING,

    MAPPING,

    BUILDING,

    WRITING,

    PENDING_WRITE,

    WRITTEN,

    PARTIAL,

    FAILED

}