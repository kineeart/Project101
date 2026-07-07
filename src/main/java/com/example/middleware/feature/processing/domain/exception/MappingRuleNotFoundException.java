package com.example.middleware.feature.processing.domain.exception;

public class MappingRuleNotFoundException
        extends RuntimeException {

    public MappingRuleNotFoundException(String message) {
        super(message);
    }
}