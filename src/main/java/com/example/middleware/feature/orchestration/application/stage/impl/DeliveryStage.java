package com.example.middleware.feature.orchestration.application.stage.impl;

import com.example.middleware.feature.delivery.application.usecase.DeliveryUseCase;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.orchestration.application.stage.PipelineStage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(300)
public class DeliveryStage implements PipelineStage {

    private final DeliveryUseCase deliveryUseCase;

    public DeliveryStage(DeliveryUseCase deliveryUseCase) {
        this.deliveryUseCase = deliveryUseCase;
    }

    @Override
    public String name() {
        return "Delivery";
    }

    @Override
    public StageResult execute(PipelineContext context) {
        System.out.println("=== DeliveryStage ===");
        return deliveryUseCase.deliver(context);
    }
}