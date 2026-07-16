package com.example.middleware.feature.delivery.infrastructure.publisher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.OutputPublisher;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;

@Component
public class AtomicOutputPublisher implements OutputPublisher {


    @Override
    public void publish(
            Path workspaceFile,
            DeliveryProfile profile) {


        try {

            validateWorkspaceFile(workspaceFile);


            Path targetDirectory =
                    Path.of(
                        profile.getOutputFolder()
                    );


            Files.createDirectories(
                    targetDirectory
            );


            Path targetFile =
                    targetDirectory.resolve(
                            workspaceFile.getFileName()
                    );


            Files.move(
                    workspaceFile,
                    targetFile,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );


        } catch (IOException e) {

            throw new RuntimeException(
                    "Atomic publish failed: "
                    + workspaceFile,
                    e
            );
        }
    }



    private void validateWorkspaceFile(
            Path workspaceFile) {


        if (!Files.exists(workspaceFile)) {

            throw new IllegalStateException(
                    "Workspace file does not exist: "
                    + workspaceFile
            );
        }


        if (!Files.isRegularFile(workspaceFile)) {

            throw new IllegalStateException(
                    "Workspace path is not a file: "
                    + workspaceFile
            );
        }
    }
}