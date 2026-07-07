package com.example.middleware.feature.processing.application.port;

import com.example.middleware.feature.processing.domain.context.MappingContext;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

public interface MappingPort {

    TransformedEvent transform(RawEvent event, MappingContext context);
}