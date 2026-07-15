package com.example.middleware.feature.transformation.infrastructure;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.transformation.domain.TransformationStep;
import org.springframework.stereotype.Component;

@Component
public class BooleanFieldTransformer implements TransformationStep {

    @Override
    public int order() {
        return 20;
    }

    @Override
    public boolean supports(FieldRule rule) {
        return rule != null && "BOOLEAN".equals(rule.getDataType());
    }

    @Override
    public Object transform(FieldRule rule, Object value) {
        if (value == null) {
            return null;
        }
        String strVal = value.toString().trim().toLowerCase();
        return "true".equals(strVal) || "1".equals(strVal);
    }
}
