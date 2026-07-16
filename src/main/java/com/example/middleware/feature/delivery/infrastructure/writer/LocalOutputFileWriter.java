package com.example.middleware.feature.delivery.infrastructure.writer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.OutputFileWriter;
import com.example.middleware.feature.delivery.application.port.WorkspaceManager;
import com.example.middleware.feature.delivery.domain.OutputFile;

@Component
public class LocalOutputFileWriter implements OutputFileWriter {

    // Task 1 — Inject WorkspaceManager
    private final WorkspaceManager workspaceManager;

    public LocalOutputFileWriter(WorkspaceManager workspaceManager) {
        this.workspaceManager = workspaceManager;
    }

    @Override
    public void write(OutputFile outputFile) {
        // Task 2 — Đổi sang Path (Lấy đường dẫn từ WorkspaceManager ở đầu method)
        Path workspaceFile = workspaceManager.workspaceFile(outputFile);

        // Task 3 — Đừng dùng java.io.FileWriter / PrintWriter nữa -> Chuyển hẳn sang NIO (Files.write)
        try {
            // Tự động tạo thư mục cha nếu chưa tồn tại trước khi ghi file vật lý
            if (workspaceFile.getParent() != null) {
                Files.createDirectories(workspaceFile.getParent());
            }
            
            // Ghi toàn bộ các dòng dữ liệu vào file vật lý với chuẩn UTF-8
            Files.write(workspaceFile, outputFile.lines(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Failed to write file via NIO: " + outputFile.fileName(), e);
        }
    }
}
