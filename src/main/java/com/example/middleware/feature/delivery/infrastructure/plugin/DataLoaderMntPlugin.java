package com.example.middleware.feature.delivery.infrastructure.plugin;

import com.example.middleware.delivery.strategy.OutputWriterStrategy;
import com.example.middleware.feature.delivery.infrastructure.formatter.CsvFormatter;
import com.example.middleware.feature.delivery.infrastructure.storage.FileStorage;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
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
    public String write(
        TransformedEvent event,
        DeliveryProfile deliveryProfile) {

        if (event == null || event.getPayload() == null) {
            throw new IllegalArgumentException("Invalid event payload");
        }

        List<String> lines = new ArrayList<>();

        lines.add(formatter.formatHeader(event));
        lines.add(formatter.formatRecord(event));
        if (deliveryProfile.isIncludeTrailer()) {

    lines.add(
            deliveryProfile.getTrailerTemplate()
    );

}
        String filePath =
        buildFilePath(deliveryProfile);

        storage.write(filePath, lines);

        return filePath;
    }



    private String buildFilePath(
        DeliveryProfile profile) {

    return profile.getOutputFolder()
            + "/"
            + profile.getFilePrefix()
            + System.currentTimeMillis()
            + "."
            + profile.getExtension();
}
}