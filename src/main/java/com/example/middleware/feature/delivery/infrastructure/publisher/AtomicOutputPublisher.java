package com.example.middleware.feature.delivery.infrastructure.publisher;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.delivery.application.port.OutputPublisher;
import com.example.middleware.feature.delivery.domain.OutputFile;

@Component
public class AtomicOutputPublisher implements OutputPublisher {

    @Override
    public void publish(OutputFile outputFile) {

        // TODO

    }

}