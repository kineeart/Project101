package com.example.middleware.feature.delivery.application.port;

import java.nio.file.Path;

import com.example.middleware.feature.delivery.domain.OutputFile;

public interface OutputFileWriter {

Path write(OutputFile outputFile);
}
