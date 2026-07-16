package com.example.middleware.feature.transformation.infrastructure;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.transformation.domain.TransformationStep;
import org.springframework.stereotype.Component;

@Component
public class XrefTransformationStep implements TransformationStep {

    @Override
    public int order() {
        return 10;
    }

    @Override
    public boolean supports(FieldRule rule) {
        // Kiểm tra xem trường cấu hình hiện tại có chứa từ điển Xref hay không
        return rule != null && rule.getXrefDictionary() != null;
    }

 @Override
public Object transform(FieldRule rule, Object value) {

    if (value == null) {
        return null;
    }

    if (rule.getXrefDictionary() == null) {
        return value;
    }

    Object mapped =
            rule.getXrefDictionary()
                    .get(value.toString());

    return mapped != null
            ? mapped
            : value;
}
}
