package com.example.middleware.feature.delivery.infrastructure.writer;

import java.io.PrintWriter;
import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.OutputFileWriter;
import com.example.middleware.feature.delivery.domain.OutputFile; // Thêm import cho OutputFile

@Component
public class LocalOutputFileWriter implements OutputFileWriter {

    @Override
    public void write(OutputFile outputFile) {
        // Sử dụng java.io.FileWriter để tránh xung đột tên với interface OutputFileWriter
        try (PrintWriter writer = new PrintWriter(new java.io.FileWriter(outputFile.filePath()))) {

            for (String line : outputFile.lines()) {
                writer.println(line);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to write file: " + outputFile.filePath(), e);
        }
    }
}
