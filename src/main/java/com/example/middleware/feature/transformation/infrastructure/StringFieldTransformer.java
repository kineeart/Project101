package com.example.middleware.feature.transformation.infrastructure;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.transformation.domain.FieldTransformer;

@Component
public class StringFieldTransformer
        implements FieldTransformer {

    @Override
    public String type() {
        return "STRING";
    }

    @Override
    public Object transform(Object value) {

        if (value == null) {
            return null;
        }

        return value.toString();
    }

}
