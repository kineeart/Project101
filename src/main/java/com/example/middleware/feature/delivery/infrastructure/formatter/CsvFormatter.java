package com.example.middleware.feature.delivery.infrastructure.formatter;

import java.util.StringJoiner;

import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

import org.springframework.stereotype.Component;

@Component
public class CsvFormatter {

    // 1. Đổi tên từ delimiter sang getDelimiter theo hướng dẫn
    private String getDelimiter(DeliveryProfile profile) {
        return profile.getDelimiter();
    }

    public String formatHeader(TransformedEvent event, DeliveryProfile profile) {
        return String.join(getDelimiter(profile), event.getPayload().keySet());
    }

    // 2. Thêm khoảng trắng giữa "String" và "formatRow"
    public String formatRow(TransformedEvent event, DeliveryProfile profile) {
        // 3. Khởi tạo StringJoiner đúng cách từ chuỗi delimiter
        StringJoiner joiner = new StringJoiner(getDelimiter(profile));

        for (Object value : event.getPayload().values()) {
            joiner.add(value == null ? "" : value.toString());
        }

        return joiner.toString();
    }

    public String formatRecord(TransformedEvent event, DeliveryProfile profile) {
        return formatRow(event, profile);
    }
}
