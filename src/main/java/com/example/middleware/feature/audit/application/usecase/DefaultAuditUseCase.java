package com.example.middleware.feature.audit.application.usecase;

import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuditUseCase implements AuditUseCase {

    @Override
    public StageResult audit(PipelineContext context) {
        return StageResult.SUCCESS;
    }
}