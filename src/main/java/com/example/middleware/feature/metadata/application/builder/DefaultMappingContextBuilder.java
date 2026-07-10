package com.example.middleware.feature.metadata.application.builder;

import com.example.middleware.feature.processing.domain.context.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class DefaultMappingContextBuilder
        implements MappingContextBuilder {

    @Override
    public MappingContext build(String profileId) {

        return new MappingContext();

    }

}