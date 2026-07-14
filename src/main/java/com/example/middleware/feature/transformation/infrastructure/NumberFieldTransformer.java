package com.example.middleware.feature.transformation.infrastructure;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.transformation.domain.FieldTransformer;

@Component
public class NumberFieldTransformer
        implements FieldTransformer {

    @Override
    public String type() {
        return "NUMBER";
    }

    @Override
    public Object transform(Object value) {

        if (value == null) {
            return null;
        }

        return Double.valueOf(value.toString());
    }
}