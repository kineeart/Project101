package com.example.middleware.feature.orchestration.application.stage.impl;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.orchestration.application.stage.PipelineStage;
import com.example.middleware.feature.orchestration.application.stage.StageOrders;
import com.example.middleware.feature.processing.application.usecase.ValidationUseCase;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

@Component
@Order(StageOrders.VALIDATION)
public class ValidationStage implements PipelineStage {
    private final ValidationUseCase validationUseCase;

    public ValidationStage(ValidationUseCase validationUseCase) {
        this.validationUseCase = validationUseCase;
    }

    @Override
    public String name() {
        return "Validation";
    }

    @Override
 
public StageResult execute(PipelineContext context) {
    return validationUseCase.validate(context);

}

}