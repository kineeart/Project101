package com.example.middleware.feature.validation.domain;

public interface FieldValidator {

    String type();

    void validate(
            String fieldName,
            Object value
    );
}