package com.example.middleware.feature.runtime.application.usecase;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.application.port.BatchHistoryRepositoryPort;
import com.example.middleware.feature.runtime.application.model.BatchDetail;
import com.example.middleware.feature.runtime.application.model.BatchSummary;
import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.history.BatchHistoryRecord;
import com.example.middleware.feature.runtime.domain.BatchStatus; // SỬA: Đổi từ .domain.batch.BatchStatus sang .domain.BatchStatus

@Service
public class DefaultGetBatchDetailUseCase implements GetBatchDetailUseCase {

    private final BatchRepositoryPort batchRepository;
    private final BatchHistoryRepositoryPort batchHistoryRepository;

    public DefaultGetBatchDetailUseCase(
            BatchRepositoryPort batchRepository,
            BatchHistoryRepositoryPort batchHistoryRepository) {
        this.batchRepository = batchRepository;
        this.batchHistoryRepository = batchHistoryRepository;
    }

    @Override
    public BatchDetail get(String batchId) {
        BatchRecord batch = batchRepository.findById(batchId);
        if (batch == null) {
            throw new IllegalArgumentException("Batch not found: " + batchId);
        }

        List<BatchHistoryRecord> history = batchHistoryRepository.findByBatchId(batchId);

        // Hết lỗi: Các kiểu dữ liệu BatchStatus giờ đã trùng khớp hoàn toàn
        return new BatchDetail(
                batch.getBatchId(),
                batch.getStatus(),
                batch.getReceivedAt(),
                batch.getUpdatedAt(),
                history
        );
    }

    @Override
    public List<BatchSummary> list() {
        return batchRepository.findAll().stream()
                .map(batch -> new BatchSummary(
                        batch.getBatchId(),
                        batch.getStatus(),
                        batch.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }
}
