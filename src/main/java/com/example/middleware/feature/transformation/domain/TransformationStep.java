package com.example.middleware.feature.transformation.domain;

import com.example.middleware.feature.metadata.domain.FieldRule;

public interface TransformationStep {

    int order();

    boolean supports(FieldRule rule);

    Object transform(FieldRule rule, Object value);
}
