package com.example.middleware.feature.processing.application.usecase;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;

public interface ValidationUseCase {

    StageResult validate(PipelineContext context);
    

}