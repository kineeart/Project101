package com.example.middleware.feature.metadata.application.builder;

import com.example.middleware.feature.processing.domain.context.MappingContext;

public interface MappingContextBuilder {

    MappingContext build(String profileId);

}