package com.example.middleware.feature.validation.infrastructure;

import com.example.middleware.feature.validation.domain.FieldValidator;
import org.springframework.stereotype.Component;

@Component
public class StringFieldValidator implements FieldValidator {

    @Override
    public String type() {
        return "STRING";
    }

    @Override
    public void validate(String fieldName, Object value) {
        if (value == null) {
            return;
        }
        
        if (!(value instanceof String)) {
            throw new IllegalArgumentException(fieldName + " must be a string");
        }
    }
}
