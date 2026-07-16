package com.example.middleware.feature.delivery.publisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.example.middleware.feature.delivery.application.model.WorkspaceArtifact;
import com.example.middleware.feature.delivery.infrastructure.publisher.AtomicOutputPublisher;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;

class AtomicOutputPublisherTest {

    // Bước 2 — Dùng hai thư mục tạm độc lập mô phỏng sát Production Runtime
    @TempDir
    Path workspace;

    @TempDir
    Path output;

    @Test
    void shouldMoveFileToOutputFolderSuccessfully() throws IOException {
        
        // Bước 3 — Tạo file thật trong thư mục Workspace tạm thời
        Path workspaceFile = workspace.resolve("ORACLE_TEST.csv");

        Files.write(
                workspaceFile,
                List.of(
                        "HEADER",
                        "ROW",
                        "TRAILER"
                )
        );

        // Đóng gói thành WorkspaceArtifact theo đúng cấu trúc Interface hiện tại của Publisher
        WorkspaceArtifact artifact = new WorkspaceArtifact(
                "ORACLE_TEST.csv", 
                workspaceFile.toString()
        );

        // Bước 4 — Chuẩn bị DeliveryProfile trỏ đến thư mục output tạm thời
        DeliveryProfile profile = new DeliveryProfile(
                output.toString(), // Publisher chỉ dùng getOutputFolder() này
                "ORACLE_",
                "csv",
                ",",
                true,
                true,
                "TRAILER"
        );

        // Bước 5 — Thực hiện Publish
        AtomicOutputPublisher publisher = new AtomicOutputPublisher();
        publisher.publish(artifact, profile);

        // Bước 6 — Verify tính đúng đắn trên FileSystem (Đúng Technical Design)
        
        // A. File ở thư mục nguồn Workspace bắt buộc phải biến mất (vì hành động là MOVE)
        assertFalse(Files.exists(workspaceFile));

        // B. File bắt buộc phải xuất hiện tại thư mục đích Output
        Path outputFile = output.resolve("ORACLE_TEST.csv");
        assertTrue(Files.exists(outputFile));

        // C. Nội dung dữ liệu được bảo toàn tuyệt đối, không bị chỉnh sửa hay mất mát
        assertEquals(
                List.of(
                        "HEADER",
                        "ROW",
                        "TRAILER"
                ),
                Files.readAllLines(outputFile)
        );
    }
}
