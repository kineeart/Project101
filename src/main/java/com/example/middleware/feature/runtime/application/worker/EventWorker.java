package com.example.middleware.feature.runtime.application.worker;

import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus;

@Service
public class EventWorker {

    private final BatchRepositoryPort batchRepository;
    // Tạm thời comment 2 dòng này lại nếu dự án của bạn chưa tạo các lớp Dispatcher 
    // private final ProcessingDispatcher dispatcher;

    public EventWorker(BatchRepositoryPort batchRepository) {
        this.batchRepository = batchRepository;
    }

      // Điền đoạn code này thay thế cho hàm poll() trống ở Checkpoint 3:
    @Scheduled(fixedDelay = 1000)
    public void poll() {
        // Quét tìm tất cả các Batch đang ở trạng thái RECEIVED
        List<BatchRecord> batches = batchRepository.findByStatus(BatchStatus.RECEIVED);

        for (BatchRecord batch : batches) {
            // Chỉ in ra màn hình Console để chứng minh Worker đang chạy độc lập
            System.out.println("Found batch: " + batch.getBatchId());
        }
    }

}
