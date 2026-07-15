package com.example.middleware.feature.processing.application;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.metadata.domain.TableRule;
import com.example.middleware.feature.processing.application.port.MappingPort;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
import com.example.middleware.feature.processing.domain.exception.MappingRuleNotFoundException;
import com.example.middleware.feature.processing.domain.validation.RequiredValidator;
import com.example.middleware.feature.transformation.application.TransformationRuntime; 
// 1. Nhập bổ sung gói ValidationRuntime vào động cơ ánh xạ
import com.example.middleware.feature.validation.application.ValidationRuntime; 

import org.springframework.stereotype.Service;

@Service
public class MappingEngine implements MappingPort {

    private final RequiredValidator validator = new RequiredValidator();
    private final TransformationRuntime runtime;
    // 2. Định nghĩa thuộc tính ValidationRuntime
    private final ValidationRuntime validationRuntime; 

    // 3. Khởi tạo Constructor tiêm (inject) đầy đủ cả 2 bộ điều hướng xử lý động
    public MappingEngine(TransformationRuntime runtime, ValidationRuntime validationRuntime) {
        this.runtime = runtime;
        this.validationRuntime = validationRuntime;
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
            String sourceFieldName = rule.getSourceField();
            Object value = event.getPayload().get(sourceFieldName);

            if (value == null) {
                value = rule.getDefaultValue();
            }

            // [NẤC 1 - TRANSFORMATION]: Chuyển đổi dữ liệu qua chuỗi Pipeline (Xref -> Ép kiểu dữ liệu gốc)
           
           System.out.println(rule.getXrefDictionary()); // Ví dụ: Chuỗi "true" -> Qua Xref thành chuỗi "1" -> Qua NumberTransformer thành số 1.0
            value = runtime.transform(rule, value);

            // Kiểm tra ràng buộc bắt buộc (Required) chung cho các trường
            validator.validate(rule, value);

            // [NẤC 2 - VALIDATION RUNTIME]: Xác thực kiểu dữ liệu của giá trị mới sau khi đã biến đổi
            if (value != null) {
                // Kiểm tra xem giá trị 1.0 nhận được có đúng định dạng NUMBER và thỏa mãn luật > 0 không
                validationRuntime.validate(rule.getDataType(), sourceFieldName, value);

                // Nếu mọi nấc kiểm tra đều hợp lệ, chính thức nạp vào payload đích để lưu xuống CSDL
                result.getPayload().put(
                        rule.getTargetField(),
                        value
                );
            }
        }

        return result;
    }
}
