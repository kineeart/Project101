package com.example.middleware.feature.transformation.domain;

public interface FieldTransformer {

    String type();

    Object transform(Object value);
}