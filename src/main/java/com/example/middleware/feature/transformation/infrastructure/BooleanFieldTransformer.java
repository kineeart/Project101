package com.example.middleware.feature.transformation.infrastructure;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.transformation.domain.FieldTransformer;

@Component
public class BooleanFieldTransformer
        implements FieldTransformer {

    @Override
    public String type() {
        return "BOOLEAN";
    }

    @Override
    public Object transform(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return value;
        }

        return Boolean.valueOf(value.toString());
    }

}
