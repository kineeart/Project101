package com.example.middleware.feature.orchestration.application.stage.impl;

import com.example.middleware.feature.audit.application.usecase.AuditUseCase;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.orchestration.application.stage.PipelineStage;
import com.example.middleware.feature.orchestration.application.stage.StageOrders;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(StageOrders.AUDIT)
public class AuditStage implements PipelineStage {

    private final AuditUseCase auditUseCase;

    public AuditStage(AuditUseCase auditUseCase) {
        this.auditUseCase = auditUseCase;
    }

    @Override
    public String name() {
        return "Audit";
    }

    @Override
    public StageResult execute(PipelineContext context) {
        return auditUseCase.audit(context);
    }
}