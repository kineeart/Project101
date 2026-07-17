package com.example.middleware.feature.validation.infrastructure;

import com.example.middleware.feature.validation.domain.FieldValidator;
import org.springframework.stereotype.Component;

@Component
public class NumberFieldValidator implements FieldValidator {

    @Override
    public String type() {
        return "NUMBER";
    }

    @Override
    public void validate(String fieldName, Object value) {
        if (value == null) {
            return;
        }

        if (!(value instanceof Number)) {
            throw new IllegalArgumentException(fieldName + " must be numeric");
        }

        double numericValue = ((Number) value).doubleValue();
        if (numericValue < 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0");
        }
    }
}
