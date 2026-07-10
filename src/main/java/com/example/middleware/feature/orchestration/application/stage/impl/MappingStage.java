package com.example.middleware.feature.orchestration.application.stage.impl;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.orchestration.application.stage.PipelineStage;
import com.example.middleware.feature.orchestration.application.stage.StageOrders;
import com.example.middleware.feature.processing.application.usecase.MappingUseCase;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
@Component
@Order(StageOrders.MAPPING)
public class MappingStage implements PipelineStage {
    private final MappingUseCase mappingUseCase;

    public MappingStage(MappingUseCase mappingUseCase) {
        this.mappingUseCase = mappingUseCase;
    }

    @Override
    public String name() {
        return "Mapping";
    }

    @Override
    public StageResult execute(PipelineContext context) {
        System.out.println("=== MappingStage ===");
        return mappingUseCase.map(context);
    }
}