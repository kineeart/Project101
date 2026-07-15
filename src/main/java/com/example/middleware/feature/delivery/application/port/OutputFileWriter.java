package com.example.middleware.feature.delivery.application.port;

import com.example.middleware.feature.delivery.domain.OutputFile;

public interface OutputFileWriter {

    void write(OutputFile outputFile);

}
