package com.example.middleware.feature.audit.application.usecase;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;

public interface AuditUseCase {

    StageResult audit(PipelineContext context);

}