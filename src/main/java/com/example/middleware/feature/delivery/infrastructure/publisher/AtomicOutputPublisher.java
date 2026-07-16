package com.example.middleware.feature.delivery.infrastructure.publisher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.OutputPublisher;
import com.example.middleware.feature.delivery.domain.PublishException;
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
           try {

    Files.move(
            workspaceFile,
            targetFile,
            StandardCopyOption.ATOMIC_MOVE
    );

} catch (AtomicMoveNotSupportedException ex) {

    Files.move(
            workspaceFile,
            targetFile,
            StandardCopyOption.REPLACE_EXISTING
    );

}

        } catch (IOException ex) {
            // Task 4: Bọc IOException thành RuntimeException để không đổi signature của interface
          throw new PublishException(
    "Failed to publish file: "
        + workspaceFile,
    ex
);
        }
        // TODO P7.2:
// replace with ATOMIC_MOVE after compatibility testing
    }
}
