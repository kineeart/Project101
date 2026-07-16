package com.example.middleware.feature.delivery.infrastructure.writer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.model.WorkspaceArtifact;
import com.example.middleware.feature.delivery.application.port.OutputFileWriter;
import com.example.middleware.feature.delivery.application.port.WorkspaceManager;
import com.example.middleware.feature.delivery.domain.OutputFile;

@Component
public class LocalOutputFileWriter implements OutputFileWriter {

    private final WorkspaceManager workspaceManager;

    public LocalOutputFileWriter(WorkspaceManager workspaceManager) {
        this.workspaceManager = workspaceManager;
    }

    @Override
    public WorkspaceArtifact write(OutputFile outputFile) {
        // Lấy đường dẫn phân giải từ WorkspaceManager ở đầu method
        Path workspaceFile = workspaceManager.workspaceFile(outputFile);

        try {
            // Tự động tạo thư mục cha nếu chưa tồn tại trước khi thực hiện ghi IO vật lý
            if (workspaceFile.getParent() != null) {
                Files.createDirectories(workspaceFile.getParent());
            }
            
            // Ghi toàn bộ dữ liệu dòng (lines) vào file vật lý với chuẩn UTF-8 sử dụng NIO
            Files.write(workspaceFile, outputFile.lines(), StandardCharsets.UTF_8);

            // ĐH ĐỔI SANG RETURN THEO HƯỚNG DẪN: Trả về đối tượng đóng gói WorkspaceArtifact
            return new WorkspaceArtifact(
                    outputFile.fileName(),
                    workspaceFile.toString()
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to write file via NIO: " + outputFile.fileName(), e);
        }
    }
}
