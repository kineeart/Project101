package com.example.middleware.feature.processing.domain.exception;

public class ValidationException
        extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}