package com.example.middleware.feature.processing.application;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.metadata.domain.TableRule;
import com.example.middleware.feature.processing.application.port.MappingPort;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
import com.example.middleware.feature.processing.domain.exception.MappingRuleNotFoundException;
import com.example.middleware.feature.processing.domain.validation.RequiredValidator;
// Import thêm lớp TransformationRuntime (bạn hãy kiểm tra lại package chính xác của lớp này trong project)
import com.example.middleware.feature.transformation.application.TransformationRuntime; 

import org.springframework.stereotype.Service;

@Service
public class MappingEngine implements MappingPort {

    private final RequiredValidator validator = new RequiredValidator();
    
    // 1. Khai báo thuộc tính TransformationRuntime
    private final TransformationRuntime runtime;

    // 2. Tạo Constructor để Spring Boot tự động Inject TransformationRuntime vào
    public MappingEngine(TransformationRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public TransformedEvent transform(
            RawEvent event,
            MappingContext context) {

        System.out.println("Source Table = " + event.getSourceTable());
        System.out.println("Rule = " + context.getRule(event.getSourceTable()));
        
        TableRule tableRule = context.getRule(event.getSourceTable());

        if (tableRule == null) {
            throw new MappingRuleNotFoundException(
                    "No mapping found for " + event.getSourceTable()
            );
        }

        TransformedEvent result = new TransformedEvent();

        result.setProfileId(event.getProfileId());
        result.setTargetTable(tableRule.getTargetTable());

        for (FieldRule rule : tableRule.getFieldRules()) {

            Object value = event.getPayload().get(rule.getSourceField());

            if (value == null) {
                value = rule.getDefaultValue();
            }

            // 3. XÓA BỎ đoạn code gọi xrefTransformer cũ.
            // THAY THẾ BẰNG cách gọi bộ runtime biến đổi dữ liệu động mới.
            value = runtime.transform(rule, value);

            validator.validate(rule, value);

            if (value != null) {
                result.getPayload().put(
                        rule.getTargetField(),
                        value
                );
            }
        }

        return result;
    }
}
