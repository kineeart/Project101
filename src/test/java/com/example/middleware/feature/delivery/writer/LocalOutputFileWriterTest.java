package com.example.middleware.feature.delivery.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.example.middleware.feature.delivery.application.model.WorkspaceArtifact;
import com.example.middleware.feature.delivery.application.port.WorkspaceManager;
import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.delivery.infrastructure.workspace.DefaultWorkspaceManager;
import com.example.middleware.feature.delivery.infrastructure.writer.LocalOutputFileWriter;
import com.example.middleware.feature.metadata.infrastructure.config.WorkspaceProperties;

class LocalOutputFileWriterTest {

    // Bước 2 — Dùng @TempDir để tạo và tự động dọn dẹp thư mục tạm của hệ thống filesystem
    @TempDir
    Path tempDir;

    @Test
    void shouldWriteOutputFileIntoWorkspace() throws IOException {

        // Bước 3 — Chuẩn bị WorkspaceManager với cấu hình trỏ vào thư mục tạm tempDir
        WorkspaceProperties properties = new WorkspaceProperties();
        properties.setFolder(tempDir.toString());

        WorkspaceManager workspaceManager = new DefaultWorkspaceManager(properties);

        // Bước 4 — Tạo Writer truyền dependency thực tế vào
        LocalOutputFileWriter writer = new LocalOutputFileWriter(workspaceManager);

        // Bước 5 — Chuẩn bị OutputFile với 3 dòng dữ liệu mẫu
        OutputFile outputFile = new OutputFile(
                "ORACLE_TEST.csv",
                List.of(
                        "HEADER",
                        "ROW1",
                        "TRAILER"
                )
        );

        // Bước 6 — Kích hoạt hành động ghi file IO thực tế
        WorkspaceArtifact artifact = writer.write(outputFile);

        // Bước 7 — Verify (Đọc file vật lý ngược lại bằng NIO để đối chiếu tính toàn vẹn)
        Path workspaceFile = tempDir.resolve("ORACLE_TEST.csv");

        // Kiểm tra file thực sự được sinh ra trên ổ đĩa tạm thời
        assertTrue(Files.exists(workspaceFile));

        // Đọc toàn bộ các dòng dữ liệu trong file vật lý lên bộ nhớ
        List<String> lines = Files.readAllLines(workspaceFile);

        // Xác thực nội dung file khớp 100% với dữ liệu cấu trúc bộ nhớ ban đầu
        assertEquals(
                List.of(
                        "HEADER",
                        "ROW1",
                        "TRAILER"
                ),
                lines
        );
        
        // Xác thực thêm cấu trúc dữ liệu trả về của WorkspaceArtifact
        assertEquals("ORACLE_TEST.csv", artifact.fileName());
        assertEquals(workspaceFile.toString(), artifact.location());
    }
}
