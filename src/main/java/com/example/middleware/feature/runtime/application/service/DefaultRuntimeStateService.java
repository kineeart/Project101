package com.example.middleware.feature.runtime.application.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.application.port.BatchHistoryRepositoryPort;
import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.history.BatchHistoryRecord;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus;
import com.example.middleware.feature.runtime.domain.state.RuntimeStateMachine;

@Service
public class DefaultRuntimeStateService implements RuntimeStateService {

    private final BatchRepositoryPort repository;
    private final RuntimeStateMachine stateMachine;
    private final BatchHistoryRepositoryPort historyRepository;

    public DefaultRuntimeStateService(
            BatchRepositoryPort repository,
            RuntimeStateMachine stateMachine,
            BatchHistoryRepositoryPort historyRepository) {
        this.repository = repository;
        this.stateMachine = stateMachine;
        this.historyRepository = historyRepository;
    }

    private void changeState(String batchId, BatchStatus nextStatus, String message) {
        BatchRecord batch = repository.findById(batchId);
        if (batch == null) {
            throw new IllegalArgumentException("Batch not found: " + batchId);
        }
        
        // 1. Duyệt qua State Machine để đổi trạng thái an toàn
        stateMachine.transit(batch, nextStatus);
        
        // 2. Lưu vào Database chính
        repository.save(batch);
        
        // 3. Tự động ghi nhận dòng lịch sử Timeline
        historyRepository.save(new BatchHistoryRecord(batchId, nextStatus, message));
    }

    @Override
    public void received(String batchId) {
        changeState(batchId, BatchStatus.RECEIVED, "Batch accepted and initialized");
    }

    @Override
    public void processing(String batchId) {
        changeState(batchId, BatchStatus.PROCESSING, "Worker claimed batch");
    }

    @Override
    public void validated(String batchId) {
        changeState(batchId, BatchStatus.VALIDATED, "Validation stage passed");
    }

    @Override
    public void mapped(String batchId) {
        changeState(batchId, BatchStatus.MAPPED, "Data mapping completed");
    }

    @Override
    public void built(String batchId) {
        changeState(batchId, BatchStatus.BUILT, "Pipeline context fully built");
    }

    @Override
    public void writing(String batchId) {
        changeState(batchId, BatchStatus.WRITING, "Start writing output to destination");
    }

    @Override
    public void written(String batchId) {
        changeState(batchId, BatchStatus.WRITTEN, "Output written successfully");
    }

    @Override
    public void failed(String batchId, String reason) {
        changeState(batchId, BatchStatus.FAILED, "Execution failed: " + reason);
    }
}
