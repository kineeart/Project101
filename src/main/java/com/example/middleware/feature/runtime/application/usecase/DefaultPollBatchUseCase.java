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
    private final ClaimBatchUseCase claimBatchUseCase; // NHÚNG THÊM BIẾN NÀY

    public DefaultPollBatchUseCase(
            BatchRepositoryPort batchRepository,
            ProcessingDispatcherRegistry dispatcherRegistry,
            ClaimBatchUseCase claimBatchUseCase) { // Inject vào Constructor
        this.batchRepository = batchRepository;
        this.dispatcherRegistry = dispatcherRegistry;
        this.claimBatchUseCase = claimBatchUseCase;
    }

    @Override
    public void poll() {
        List<BatchRecord> batches = batchRepository.findByStatus(BatchStatus.RECEIVED);

        for (BatchRecord batch : batches) {
            // SỬA ĐỔI: Thực hiện Claim trước khi Dispatch
            BatchRecord claimed = claimBatchUseCase.claim(batch.getBatchId());

            // Nếu một Worker khác đã nhanh tay claim trước, bỏ qua sang batch tiếp theo
            if (claimed == null) {
                continue;
            }

            // Chỉ đẩy đi xử lý khi đã claim thành công mang nhãn PROCESSING
            dispatcherRegistry
                    .defaultDispatcher()
                    .dispatch(claimed.getBatchId());
        }
    }
}
