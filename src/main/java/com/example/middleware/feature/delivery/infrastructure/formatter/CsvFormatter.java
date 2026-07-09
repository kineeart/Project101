package com.example.middleware.feature.delivery.infrastructure.formatter;

import java.util.StringJoiner;

import com.example.middleware.feature.processing.domain.event.TransformedEvent;

public class CsvFormatter {

    private static final String DELIMITER = ",";

    public String formatHeader(TransformedEvent event) {
        return String.join(DELIMITER, event.getPayload().keySet());
    }

    public String formatRow(TransformedEvent event) {
        StringJoiner joiner = new StringJoiner(DELIMITER);

        for (Object value : event.getPayload().values()) {
            joiner.add(value == null ? "" : value.toString());
        }

        return joiner.toString();
    }

    public String formatRecord(TransformedEvent event) {
        return formatRow(event);
    }
}