package com.example.middleware.feature.ingestion.application.factory;

import java.util.Map;

import com.example.middleware.feature.processing.domain.event.RawEvent;

public interface EventFactory {

    RawEvent create(Map<String, Object> request);

}
