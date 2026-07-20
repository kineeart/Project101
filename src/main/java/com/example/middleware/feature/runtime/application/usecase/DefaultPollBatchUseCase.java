package com.example.middleware.feature.runtime.application.usecase;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.middleware.feature.intake.application.registry.ProcessingDispatcherRegistry;
import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus;

@Service
public class DefaultPollBatchUseCase implements PollBatchUseCase {

    private final BatchRepositoryPort batchRepository;
    private final ProcessingDispatcherRegistry dispatcherRegistry;

    public DefaultPollBatchUseCase(
            BatchRepositoryPort batchRepository,
            ProcessingDispatcherRegistry dispatcherRegistry) {
        this.batchRepository = batchRepository;
        this.dispatcherRegistry = dispatcherRegistry;
    }

    @Override
    public void poll() {
        // 1. Quét tìm tất cả các Batch đang xếp hàng ở trạng thái RECEIVED
        List<BatchRecord> batches = batchRepository.findByStatus(BatchStatus.RECEIVED);

        // 2. Duyệt qua từng Batch và đẩy qua Dispatcher để kích hoạt Pipeline chạy ngầm
        for (BatchRecord batch : batches) {
            dispatcherRegistry
                    .defaultDispatcher()
                    .dispatch(batch.getBatchId());
        }
    }
}
