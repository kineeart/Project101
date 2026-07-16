package com.example.middleware.feature.delivery.infrastructure.workspace;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.WorkspaceManager;
import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.metadata.infrastructure.config.WorkspaceProperties;

@Component
public class DefaultWorkspaceManager implements WorkspaceManager {

    private final WorkspaceProperties workspaceProperties;

    public DefaultWorkspaceManager(WorkspaceProperties workspaceProperties) {
        this.workspaceProperties = workspaceProperties;
    }

    @Override
    public Path workspaceFile(OutputFile outputFile) {
        // Chỉ lấy đường dẫn cấu hình thư mục gốc từ Properties
        Path workspace = Path.of(workspaceProperties.getFolder());

        // Phân giải ra Path cuối cùng của file và trả về, không thực hiện IO hay tạo folder tại đây
        return workspace.resolve(outputFile.fileName());
    }
}
