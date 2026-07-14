package com.example.middleware.feature.validation.application;

import com.example.middleware.feature.validation.domain.FieldValidator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidationRuntime {

    // 1. Khai báo kho lưu trữ Registry (Đợt trước bạn bị thiếu dòng này)
    private final Map<String, FieldValidator> validatorRegistry = new HashMap<>();

    // 2. Constructor nhận vào danh sách tất cả các Validator có trong hệ thống
    public ValidationRuntime(List<FieldValidator> validators) {
        if (validators != null) {
            for (FieldValidator validator : validators) {
                // Tự động đăng ký với Key là: NUMBER, STRING, BOOLEAN
                this.validatorRegistry.put(validator.type().toUpperCase(), validator);
            }
        }
    }

    // 3. Phương thức validate xử lý điều hướng động
    public void validate(String dataType, String fieldName, Object value) {
        if (dataType == null) {
            return;
        }

        FieldValidator validator = validatorRegistry.get(dataType.toUpperCase());
        
        if (validator == null) {
            throw new IllegalArgumentException("Unsupported data type validation: " + dataType);
        }

        validator.validate(fieldName, value);
    }
}
