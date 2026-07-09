package com.example.middleware.feature.delivery.application.usecase;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;

public interface DeliveryUseCase {

    StageResult deliver(PipelineContext context);

}