package com.example.middleware.feature.monitoring.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.monitoring.application.port.ExecutionHistoryRepository;
import com.example.middleware.feature.monitoring.domain.ExecutionHistoryRecord;

@Service
public class GetExecutionHistoryUseCase {

    private final ExecutionHistoryRepository repository;

    public GetExecutionHistoryUseCase(
            ExecutionHistoryRepository repository) {

        this.repository = repository;
    }

    public List<ExecutionHistoryRecord> get(
            String executionId) {

        return repository.findByExecutionId(
                executionId
        );
    }
}