package com.example.middleware.feature.orchestration.application.port;

import java.util.List;

import com.example.middleware.feature.orchestration.domain.Execution;

public interface ExecutionRepositoryPort {

    void save(Execution execution);

    Execution findById(String executionId);

    List<Execution> findAll();
}
