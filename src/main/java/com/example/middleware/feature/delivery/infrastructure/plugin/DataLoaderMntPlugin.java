package com.example.middleware.feature.delivery.infrastructure.plugin;

import com.example.middleware.delivery.strategy.OutputWriterStrategy;
import com.example.middleware.feature.delivery.infrastructure.formatter.CsvFormatter;
import com.example.middleware.feature.delivery.infrastructure.storage.FileStorage;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoaderMntPlugin implements OutputWriterStrategy {

    private final CsvFormatter formatter;
private final FileStorage storage;

public DataLoaderMntPlugin(
        CsvFormatter formatter,
        FileStorage storage) {

    this.formatter = formatter;
    this.storage = storage;
}

    @Override
    public String type() {
        return "ORACLE_CSV";
    }

    @Override
    public String write(TransformedEvent event) {

        if (event == null || event.getPayload() == null) {
            throw new IllegalArgumentException("Invalid event payload");
        }

        List<String> lines = new ArrayList<>();

        lines.add(formatter.formatHeader(event));
        lines.add(formatter.formatRecord(event));
        lines.add("TRAILER||RECORD_COUNT|1");

        String filePath =
                "D:/Xcenter/inbound/ORACLE_" +
                        System.currentTimeMillis() +
                        ".csv";

        storage.write(filePath, lines);

        return filePath;
    }
}