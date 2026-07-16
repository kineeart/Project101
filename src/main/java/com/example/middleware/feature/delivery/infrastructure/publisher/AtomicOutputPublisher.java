package com.example.middleware.feature.delivery.infrastructure.publisher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.OutputPublisher;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;

@Component
public class AtomicOutputPublisher implements OutputPublisher {

    @Override
    public void publish(Path workspaceFile, DeliveryProfile profile) {
        try {
            // Task 1: Tính toán đường dẫn đích dựa vào DeliveryProfile, không hardcode
            Path targetDirectory = Path.of(profile.getOutputFolder());
            Path targetFile = targetDirectory.resolve(workspaceFile.getFileName());

            // Task 2: Kiểm tra và tạo thư mục đích nếu chưa tồn tại
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            // Task 3: Di chuyển file từ Workspace sang OutputFolder (chưa dùng ATOMIC_MOVE để test luồng)
            Files.move(workspaceFile, targetFile);

        } catch (IOException ex) {
            // Task 4: Bọc IOException thành RuntimeException để không đổi signature của interface
            throw new RuntimeException("Publish failed", ex);
        }
        // TODO P7.2:
// replace with ATOMIC_MOVE after compatibility testing
    }
}
