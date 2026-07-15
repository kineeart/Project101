package com.example.middleware.feature.delivery.infrastructure.plugin;

import com.example.middleware.delivery.strategy.OutputWriterStrategy;
import com.example.middleware.feature.delivery.application.port.FileBuilder;
import com.example.middleware.feature.delivery.domain.OutputFile; // Thêm import cho lớp chứa dữ liệu đầu ra file
import com.example.middleware.feature.delivery.infrastructure.storage.FileStorage;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

import org.springframework.stereotype.Component;

@Component
public class DataLoaderMntPlugin implements OutputWriterStrategy {

    // Bước 5.1: Thay thế CsvFormatter bằng FileBuilder làm dependency mới
    private final FileBuilder fileBuilder;
    private final FileStorage storage;

    // Bước 5.2: Constructor được cập nhật khớp với danh sách thuộc tính mới
    public DataLoaderMntPlugin(
            FileBuilder fileBuilder,
            FileStorage storage) {
        this.fileBuilder = fileBuilder;
        this.storage = storage;
    }

    @Override
    public String type() {
        return "ORACLE_CSV";
    }

    @Override
    public String write(
            TransformedEvent event,
            DeliveryProfile deliveryProfile) {

        // Giữ lại bước validate đầu vào quan trọng
        if (event == null || event.getPayload() == null) {
            throw new IllegalArgumentException("Invalid event payload");
        }

        // Bước 5.3: Xóa toàn bộ hơn 20 dòng logic tạo danh sách dòng cũ, giao nhiệm vụ cho fileBuilder
        OutputFile outputFile = fileBuilder.build(event, deliveryProfile);

        // Bước 5.4: Đổi từ việc truyền biến rời rạc sang đọc từ record dữ liệu của outputFile
        storage.write(
                outputFile.filePath(),
                outputFile.lines()
        );

        // Bước 5.5: Thay đổi giá trị trả về tương ứng sang filePath() mới tạo ra từ builder
        return outputFile.filePath();
    }

    // Bước 5.6: Đã loại bỏ hoàn toàn phương thức buildFilePath() vì nó đã được chuyển giao cho CsvFileBuilder
}
