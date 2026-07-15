package com.example.middleware.feature.transformation.infrastructure;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.transformation.domain.TransformationStep;
import org.springframework.stereotype.Component;

@Component
public class NumberFieldTransformer implements TransformationStep {

    @Override
    public int order() {
        return 20; // Chạy sau Xref
    }

    @Override
    public boolean supports(FieldRule rule) {
        return rule != null && "NUMBER".equals(rule.getDataType());
    }

    @Override
    public Object transform(FieldRule rule, Object value) {
        if (value == null) {
            return null;
        }
        
        // Xử lý ép kiểu dữ liệu từ chuỗi (do Xref dịch ra) hoặc số thô thành kiểu Number chuẩn
        if (value instanceof Number) {
            return value;
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot transform value to Number: " + value);
        }
    }
}
