package com.example.middleware.delivery;

import com.example.middleware.feature.delivery.application.factory.OutputWriterFactory;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class DeliveryTest {

    @Test
    public void test_csv_output() {

        TransformedEvent event = new TransformedEvent();
        event.setProfileId("P1");
        event.setTargetTable("POS");

        Map<String, Object> payload = new HashMap<>();
        payload.put("ITEM_ID", "A001");
        payload.put("PRICE", 10000);
        payload.put("LOCATION", "HCM");

        event.setPayload(payload);

        OutputWriterFactory factory = new OutputWriterFactory();

        String filePath =
                factory.getWriter("ORACLE_CSV")
                        .write(event);

        System.out.println("FILE CREATED: " + filePath);
    }
}