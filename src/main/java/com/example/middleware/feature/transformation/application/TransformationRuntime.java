package com.example.middleware.feature.transformation.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.transformation.domain.FieldTransformer;

@Service
public class TransformationRuntime {

    private final Map<String, FieldTransformer> transformers;

    public TransformationRuntime(
            List<FieldTransformer> plugins) {

        transformers =
                plugins.stream()

                        .collect(Collectors.toMap(
                                FieldTransformer::type,
                                t -> t
                        ));
    }

    public Object transform(
            FieldRule rule,
            Object value) {

        FieldTransformer transformer =
                transformers.get(rule.getDataType());

        if (transformer == null) {
            return value;
        }

        return transformer.transform(value);
    }

}
