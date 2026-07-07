package com.example.middleware.feature.processing.domain.validation;

import com.example.middleware.feature.processing.domain.exception.ValidationException;
import com.example.middleware.metadata.model.FieldRule;

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