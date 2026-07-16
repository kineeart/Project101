package com.example.middleware.feature.delivery.infrastructure.plugin;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.DeliveryPlugin;
import com.example.middleware.feature.delivery.application.port.FileBuilder;
import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

@Component
public class DataLoaderMntPlugin implements DeliveryPlugin {

    private final FileBuilder fileBuilder;

    // THEO KIẾN TRÚC MỚI: Plugin chỉ làm nhiệm vụ Build dữ liệu bộ nhớ.
    // Loại bỏ hoàn toàn các thành phần IO như OutputFileWriter, OutputPublisher khỏi đây.
    public DataLoaderMntPlugin(FileBuilder fileBuilder) {
        this.fileBuilder = fileBuilder;
    }

    @Override
    public String type() {
        return "ORACLE_CSV";
    }

    @Override
    public OutputFile build(
            TransformedEvent event,
            DeliveryProfile deliveryProfile) {

        if (event == null || event.getPayload() == null) {
            throw new IllegalArgumentException("Invalid event payload");
        }

        // Tạo ra object OutputFile chứa trọn vẹn data lines và fileName từ fileBuilder độc lập
        // ĐÃ SỬA: Trả về trực tiếp đối tượng sau khi build, không thực hiện ghi file vật lý (IO) tại đây.
        return fileBuilder.build(event, deliveryProfile);
    }
}
