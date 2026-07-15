package com.example.middleware.feature.metadata.application.validation;

public interface MetadataValidator<T> {

    void validate(T metadata);

}