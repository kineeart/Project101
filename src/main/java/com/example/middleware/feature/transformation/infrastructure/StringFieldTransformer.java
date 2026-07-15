package com.example.middleware.feature.transformation.infrastructure;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.transformation.domain.TransformationStep;
import org.springframework.stereotype.Component;

@Component
public class StringFieldTransformer implements TransformationStep {

    @Override
    public int order() {
        return 20;
    }

    @Override
    public boolean supports(FieldRule rule) {
        return rule != null && "STRING".equals(rule.getDataType());
    }

    @Override
    public Object transform(FieldRule rule, Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}
