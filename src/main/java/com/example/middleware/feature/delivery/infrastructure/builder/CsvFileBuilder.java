package com.example.middleware.feature.delivery.infrastructure.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.FileBuilder;
import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.delivery.infrastructure.formatter.CsvFormatter;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

@Component
public class CsvFileBuilder implements FileBuilder {

    private final CsvFormatter formatter;

    public CsvFileBuilder(CsvFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public OutputFile build(
            TransformedEvent event,
            DeliveryProfile deliveryProfile) {

        List<String> lines = new ArrayList<>();

        // Thêm Header
        lines.add(formatter.formatHeader(event, deliveryProfile));
        
        // Thêm Record/Row
        lines.add(formatter.formatRecord(event, deliveryProfile));

        // Thêm Trailer nếu cấu hình yêu cầu
        if (deliveryProfile.isIncludeTrailer()) {
            lines.add(deliveryProfile.getTrailerTemplate());
        }

        // Tạo đường dẫn file
        String filePath = buildFilePath(deliveryProfile);
        
        // ĐÃ SỬA: Trả về đối tượng OutputFile nằm trọn vẹn bên trong method build()
        return new OutputFile(filePath, lines);
    }

    private String buildFilePath(DeliveryProfile profile) {
        return profile.getOutputFolder()
                + "/"
                + profile.getFilePrefix()
                + System.currentTimeMillis()
                + "."
                + profile.getExtension();
    }
}
