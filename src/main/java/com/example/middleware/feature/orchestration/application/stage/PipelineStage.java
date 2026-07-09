package com.example.middleware.feature.orchestration.application.stage;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;

public interface PipelineStage {

    String name();

StageResult execute(PipelineContext context);

}