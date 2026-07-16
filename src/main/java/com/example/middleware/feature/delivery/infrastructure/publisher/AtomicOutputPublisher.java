package com.example.middleware.feature.delivery.infrastructure.publisher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.model.WorkspaceArtifact;
import com.example.middleware.feature.delivery.application.port.OutputPublisher;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;

@Component
public class AtomicOutputPublisher implements OutputPublisher {

    @Override
    public void publish(WorkspaceArtifact artifact, DeliveryProfile profile) {
        // ĐÃ SỬA THEO HƯỚNG DẪN: Infrastructure chịu trách nhiệm convert chuỗi location thành Path
        Path workspaceFile = Path.of(artifact.location());

        try {
            // Thực hiện validate kiểm tra file vật lý tồn tại
            validateWorkspaceFile(workspaceFile);

            // Tính toán đường dẫn thư mục đích từ cấu hình profile
            Path targetDirectory = Path.of(profile.getOutputFolder());

            // Tạo thư mục đích nếu chưa tồn tại
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            // Phân giải ra đường dẫn file đích cuối cùng dựa trên tên file trong workspace
            Path targetFile = targetDirectory.resolve(workspaceFile.getFileName());

            // Tiến hành di chuyển file an toàn bằng ATOMIC_MOVE
            Files.move(
                    workspaceFile,
                    targetFile,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException("Atomic publish failed for artifact: " + artifact.fileName(), e);
        }
    }

    private void validateWorkspaceFile(Path workspaceFile) {
        if (!Files.exists(workspaceFile)) {
            throw new IllegalStateException("Workspace file does not exist: " + workspaceFile);
        }

        if (!Files.isRegularFile(workspaceFile)) {
            throw new IllegalStateException("Workspace path is not a file: " + workspaceFile);
        }
    }
}
