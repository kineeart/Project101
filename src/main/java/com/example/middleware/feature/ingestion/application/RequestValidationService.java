package com.example.middleware.feature.ingestion.application;

import com.example.middleware.feature.metadata.application.MetadataService;
import com.example.middleware.feature.metadata.application.resolver.ProfileResolver;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.metadata.domain.TableRule;
import com.example.middleware.feature.validation.application.ValidationRuntime;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RequestValidationService {

    private final MetadataService metadataService;
    private final ProfileResolver profileResolver;
    private final ValidationRuntime validationRuntime; // 1. Inject thêm ValidationRuntime

    public RequestValidationService(
            MetadataService metadataService,
            ProfileResolver profileResolver,
            ValidationRuntime validationRuntime) {
        this.metadataService = metadataService;
        this.profileResolver = profileResolver;
        this.validationRuntime = validationRuntime;
    }

    public void validate(Map<String, Object> request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is null");
        }
        if (request.get("eventId") == null) {
            throw new IllegalArgumentException("Missing eventId");
        }

        String profileId = profileResolver.resolve(request);
        EventMetadata metadata = metadataService.resolveEventMetadata(profileId);
        
        if (metadata == null || metadata.getTableRules() == null) {
            return;
        }

        for (TableRule table : metadata.getTableRules()) {
            if (table.getFieldRules() == null) continue;

            for (FieldRule field : table.getFieldRules()) {
                String sourceFieldName = field.getSourceField();
                Object value = request.get(sourceFieldName);

                // Kiểm tra thuộc tính bắt buộc (Required) chung cho mọi trường
                if (field.isRequired() && value == null) {
                    throw new IllegalArgumentException("Missing " + sourceFieldName);
                }

                // 2. Định hướng kiểm tra kiểu dữ liệu ĐỘNG qua ValidationRuntime 
                // Không còn bất kỳ câu lệnh if ("NUMBER") nào ở đây nữa!
                if (value != null) {
                    validationRuntime.validate(field.getDataType(), sourceFieldName, value);
                }
            }
        }
    }
}
