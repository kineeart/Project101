package com.example.middleware.feature.runtime.application;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;

@Service
public class DefaultRuntimeStateManager implements RuntimeStateManager {

    private final EventRepositoryPort repository;

    public DefaultRuntimeStateManager(EventRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void enterStage(String batchId, String stageName) {
        EventRecord record = repository.findById(batchId);
        if (record == null) {
            return;
        }

        record.enterStage(stageName);
        repository.save(record);
    }

    @Override
    public void complete(String batchId, String filePath) {
        EventRecord record = repository.findById(batchId);
        if (record == null) {
            return;
        }

        record.markWritten(filePath);
        repository.save(record);
    }

    @Override
    public void fail(String batchId, String message) {
        EventRecord record = repository.findById(batchId);
        if (record == null) {
            return;
        }

        record.markFailed(message);
        repository.save(record);
    }
}
