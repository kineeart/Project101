package com.example.middleware.feature.delivery.infrastructure.plugin;

import com.example.middleware.delivery.strategy.OutputWriterStrategy;
import com.example.middleware.feature.delivery.application.port.FileBuilder;
import com.example.middleware.feature.delivery.application.port.OutputFileWriter; // Import interface mới
import com.example.middleware.feature.delivery.application.port.OutputPublisher;
import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
import com.example.middleware.feature.delivery.domain.DeliveryResult;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

@Component
public class DataLoaderMntPlugin implements OutputWriterStrategy {

    private final FileBuilder fileBuilder;
    private final OutputFileWriter fileWriter; // Thay thế hoàn toàn FileStorage bằng OutputFileWriter
    private final OutputPublisher outputPublisher;
    // Cập nhật Constructor nhận đúng cặp dependency mới
   public DataLoaderMntPlugin(
        FileBuilder fileBuilder,
        OutputFileWriter fileWriter,
        OutputPublisher outputPublisher) {

    this.fileBuilder = fileBuilder;
    this.fileWriter = fileWriter;
    this.outputPublisher = outputPublisher;
}
    @Override
    public String type() {
        return "ORACLE_CSV";
    }

    @Override
    public DeliveryResult write(
            TransformedEvent event,
            DeliveryProfile deliveryProfile) {

        if (event == null || event.getPayload() == null) {
            throw new IllegalArgumentException("Invalid event payload");
        }

        // Tạo ra object OutputFile chứa trọn vẹn data và path
        OutputFile outputFile = fileBuilder.build(event, deliveryProfile);

        // Truyền thẳng cả object outputFile vào hàm write gọn gàng
       Path workspaceFile =
        fileWriter.write(outputFile);

outputPublisher.publish(
        workspaceFile,
        deliveryProfile
);

return new DeliveryResult(
        outputFile.fileName()
);    }
}
