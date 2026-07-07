package com.example.middleware.feature.processing.domain.transformation;

import java.util.Map;

public class XrefTransformer {

    public Object transform(
            Object value,
            Map<String, String> xref) {

        if (value == null || xref == null) {
            return value;
        }

        return xref.getOrDefault(
                value.toString(),
                value.toString()
        );
    }
}