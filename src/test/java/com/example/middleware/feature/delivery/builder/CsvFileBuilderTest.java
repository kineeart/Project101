package com.example.middleware.feature.delivery.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.delivery.infrastructure.builder.CsvFileBuilder;
import com.example.middleware.feature.delivery.infrastructure.formatter.CsvFormatter;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

class CsvFileBuilderTest {

    @Test
    void shouldBuildOutputFileSuccessfully() {

        CsvFormatter formatter = new CsvFormatter();
        CsvFileBuilder builder = new CsvFileBuilder(formatter);

        TransformedEvent event = new TransformedEvent();

        event.setProfileId("PROFILE_1");
        event.setTargetTable("MNT");

        Map<String, Object> payload = new LinkedHashMap<>();

        payload.put("ITEM", "1001");
        payload.put("PRICE", 25000);
        payload.put("SHOP", "HCM01");

        event.setPayload(payload);

        DeliveryProfile profile =
                new DeliveryProfile(
                        "D:/ignore",
                        "ORACLE_",
                        "csv",
                        ",",
                        true,
                        true,
                        "TRAILER||RECORD_COUNT|{count}"
                );

        OutputFile outputFile =
                builder.build(event, profile);

        assertNotNull(outputFile);

        assertNotNull(outputFile.fileName());

        assertTrue(outputFile.fileName().startsWith("ORACLE_"));

        assertTrue(outputFile.fileName().endsWith(".csv"));

        assertEquals(3, outputFile.lines().size());

        assertEquals(
                "ITEM,PRICE,SHOP",
                outputFile.lines().get(0)
        );

        assertEquals(
                "1001,25000,HCM01",
                outputFile.lines().get(1)
        );

        assertEquals(
                "TRAILER||RECORD_COUNT|{count}",
                outputFile.lines().get(2)
        );
    }
    @Test
void shouldBuildWithoutTrailerWhenTrailerDisabled() {

    CsvFormatter formatter = new CsvFormatter();
    CsvFileBuilder builder = new CsvFileBuilder(formatter);

    TransformedEvent event = new TransformedEvent();

    Map<String, Object> payload = new LinkedHashMap<>();

    payload.put("ITEM", "1001");
    payload.put("PRICE", 25000);
    payload.put("SHOP", "HCM01");

    event.setPayload(payload);

    DeliveryProfile profile =
            new DeliveryProfile(
                    "D:/ignore",
                    "ORACLE_",
                    "csv",
                    ",",
                    true,
                    false,   // <-- tắt trailer
                    "TRAILER||RECORD_COUNT|{count}"
            );

    OutputFile outputFile =
            builder.build(event, profile);

    assertEquals(
            2,
            outputFile.lines().size()
    );

    assertEquals(
            "ITEM,PRICE,SHOP",
            outputFile.lines().get(0)
    );

    assertEquals(
            "1001,25000,HCM01",
            outputFile.lines().get(1)
    );
}
}