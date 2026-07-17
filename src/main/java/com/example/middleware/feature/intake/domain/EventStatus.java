package com.example.middleware.feature.intake.domain;

public enum EventStatus {

    RECEIVED,

    PROCESSING,

    VALIDATING,

    MAPPING,

    BUILDING,

    WRITING,

    WRITTEN,

    PARTIAL,

    FAILED
}