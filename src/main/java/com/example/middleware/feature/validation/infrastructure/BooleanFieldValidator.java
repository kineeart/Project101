package com.example.middleware.feature.validation.infrastructure;

import com.example.middleware.feature.validation.domain.FieldValidator;
import org.springframework.stereotype.Component;

@Component
public class BooleanFieldValidator implements FieldValidator {

    @Override
    public String type() {
        return "BOOLEAN";
    }

    @Override
    public void validate(String fieldName, Object value) {
        if (value == null) {
            return;
        }

        if (!(value instanceof Boolean)) {
            throw new IllegalArgumentException(fieldName + " must be a boolean");
        }
    }
}
