package com.example.middleware.core.mapping;

import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.metadata.domain.TableRule;
import com.example.middleware.feature.processing.application.MappingEngine;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MappingEngineTest {

@Test
public void test_missing_mapping_should_throw_exception() {

    RawEvent event = new RawEvent(
            "EVT003",
            "PROFILE_1",
            "UNKNOWN_TABLE",
            new HashMap<>()
    );

    MappingContext context = new MappingContext();

    MappingEngine engine = new MappingEngine();

    assertThrows(
            RuntimeException.class,
            () -> engine.transform(event, context)
    );
}
}