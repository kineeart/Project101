package com.example.middleware.feature.delivery.workspace;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.delivery.infrastructure.workspace.DefaultWorkspaceManager;
import com.example.middleware.feature.metadata.infrastructure.config.WorkspaceProperties;

class DefaultWorkspaceManagerTest {

    @Test
    void shouldResolveWorkspaceFile() {

        WorkspaceProperties properties =
                new WorkspaceProperties();

        properties.setFolder(
                "D:/middleware/workspace"
        );

        DefaultWorkspaceManager manager =
                new DefaultWorkspaceManager(
                        properties
                );

        OutputFile outputFile =
                new OutputFile(
                        "ORACLE_123.csv",
                        List.of()
                );

        Path workspacePath =
                manager.workspaceFile(
                        outputFile
                );

        assertEquals(

                Path.of(
                        "D:/middleware/workspace",
                        "ORACLE_123.csv"
                ),

                workspacePath

        );
    }
}