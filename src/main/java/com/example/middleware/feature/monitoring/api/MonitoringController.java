package com.example.middleware.feature.monitoring.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.middleware.feature.monitoring.application.GetExecutionHistoryUseCase;
import com.example.middleware.feature.monitoring.domain.ExecutionHistoryRecord;

@RestController
public class MonitoringController {

    private final GetExecutionHistoryUseCase useCase;

    public MonitoringController(
            GetExecutionHistoryUseCase useCase) {

        this.useCase = useCase;
    }

    @GetMapping(
            "/api/v1/events/{executionId}"
    )
    public List<ExecutionHistoryRecord> history(
            @PathVariable
            String executionId) {

        return useCase.get(executionId);
    }
}