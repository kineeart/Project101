package com.example.middleware.feature.intake.application.usecase;

import org.springframework.stereotype.Service;
import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.processing.domain.event.RawEvent;
import com.example.middleware.feature.runtime.application.factory.BatchFactory;
import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.history.BatchHistoryRecord;
import com.example.middleware.feature.runtime.application.port.BatchHistoryRepositoryPort;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus;

@Service
public class DefaultReceiveEventUseCase implements ReceiveEventUseCase {

    private final EventRepositoryPort repository;
    private final BatchFactory batchFactory;
    private final BatchRepositoryPort batchRepository;
    private final BatchHistoryRepositoryPort historyRepository; // Thêm history repository

    public DefaultReceiveEventUseCase(
            EventRepositoryPort repository, 
            BatchFactory batchFactory, 
            BatchRepositoryPort batchRepository,
            BatchHistoryRepositoryPort historyRepository) {
        this.repository = repository;
        this.batchFactory = batchFactory;
        this.batchRepository = batchRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public EventRecord receive(RawEvent event) {
        // 1. Tạo và lưu EventRecord
        EventRecord record = new EventRecord(event);
        repository.save(record);
        
        // 2. Tạo và lưu BatchRecord
        BatchRecord batch = batchFactory.create(event);
        batchRepository.save(batch);
        
        // 3. Ghi nhận lịch sử trạng thái RECEIVED cốt lõi của Timeline
        historyRepository.save(
            new BatchHistoryRecord(
                batch.getBatchId(),
                BatchStatus.RECEIVED,
                "Batch accepted"
            )
        );
        
        // XÓA HOÀN TOÀN MỌI DÒNG DISPATCHER TẠI ĐÂY
        // Luồng kết thúc ngay lập tức để Controller phản hồi HTTP 202
        return record;
    }
}
