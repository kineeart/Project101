package com.example.middleware.feature.runtime.application.usecase;

import org.springframework.stereotype.Service;
import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus;
import com.example.middleware.feature.runtime.domain.state.RuntimeStateMachine;

@Service
public class DefaultClaimBatchUseCase implements ClaimBatchUseCase {

    private final BatchRepositoryPort repository;
    private final RuntimeStateMachine stateMachine;

    public DefaultClaimBatchUseCase(
            BatchRepositoryPort repository, 
            RuntimeStateMachine stateMachine) {
        this.repository = repository;
        this.stateMachine = stateMachine;
    }

    @Override
    public BatchRecord claim(String batchId) {
        BatchRecord batch = repository.findById(batchId);
        
        if (batch == null) {
            return null;
        }

        // Nếu trạng thái đã bị đổi bởi luồng khác (không còn là RECEIVED), hủy bỏ claim
        if (batch.getStatus() != BatchStatus.RECEIVED) {
            return null;
        }

        // Chuyển dịch trạng thái an toàn qua State Machine sang PROCESSING
        stateMachine.transit(batch, BatchStatus.PROCESSING);
        repository.save(batch);

        return batch;
    }
}
