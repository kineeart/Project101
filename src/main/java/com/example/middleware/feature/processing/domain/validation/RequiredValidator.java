package com.example.middleware.feature.processing.domain.validation;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.processing.domain.exception.ValidationException;

public class RequiredValidator {

    public void validate(FieldRule rule,
                         Object value) {

        if (rule.isRequired() && value == null) {
            throw new ValidationException(
                    "Required field missing: "
                            + rule.getTargetField()
            );
        }
    }
}