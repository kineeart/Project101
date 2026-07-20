package com.example.middleware.feature.ingestion.application;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.builder.PipelineContextBuilder;
import com.example.middleware.feature.orchestration.application.PipelineContext; 
import com.example.middleware.feature.runtime.application.service.RuntimeStateService;
import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort; // Thêm import này
import com.example.middleware.feature.runtime.domain.BatchRecord; // Thêm import này
import com.example.middleware.feature.runtime.domain.batch.BatchStatus; // Thêm import này

import org.springframework.stereotype.Service;

@Service
public class ProcessEventUseCase {

    private final EventRepositoryPort repository;
    private final PipelineContextBuilder contextBuilder;
    private final Pipeline pipeline;
    private final RuntimeStateService stateService;
    private final BatchRepositoryPort batchRepository; // Bổ sung để load batch kiểm tra trạng thái

    public ProcessEventUseCase(
            EventRepositoryPort repository,
            PipelineContextBuilder contextBuilder,
            Pipeline pipeline,
            RuntimeStateService stateService,
            BatchRepositoryPort batchRepository) { // Nhận thêm vào đây
        this.repository = repository;
        this.contextBuilder = contextBuilder;
        this.pipeline = pipeline;
        this.stateService = stateService;
        this.batchRepository = batchRepository;
    }

    public void process(String eventId) {
        // Tìm Event và validate
        EventRecord record = repository.findById(eventId);
        if (record == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }

        // Bước 5: BỔ SUNG GUARD - Load Batch lên kiểm tra nhãn Claim
        BatchRecord batch = batchRepository.findById(eventId);
        if (batch == null) {
            throw new IllegalArgumentException("Batch not found: " + eventId);
        }

        // Nếu Batch chưa được bốc bởi một Claim hợp lệ, nghiêm cấm chạy Pipeline
        if (batch.getStatus() != BatchStatus.PROCESSING) {
            throw new IllegalStateException("Batch has not been claimed: " + eventId);
        }

        try {
            // XÓA BỎ LỆNH stateService.processing CŨ TẠI ĐÂY (Vì Claim đã lo rồi)
            
            // Đồng bộ trạng thái của EventRecord tương ứng
            record.markProcessing();
            repository.save(record);

            // == GIAI ĐOẠN 2: VALIDATED ==
            stateService.validated(eventId);

            // Build PipelineContext từ RawEvent của record
            PipelineContext context = contextBuilder.build(record.getRawEvent());

            // == BƯỚC 3: MAPPED & BUILT ==
            stateService.mapped(eventId);
            stateService.built(eventId);

            // == BƯỚC 4: WRITING ==
            stateService.writing(eventId);

            // Chạy Pipeline
            pipeline.execute(context);

            // == BƯỚC 5: WRITTEN (THÀNH CÔNG) ==
            record.markWritten(context.getFilePath());
            repository.save(record);
            stateService.written(eventId);

        } catch (Exception ex) {
            // == BƯỚC 6: FAILED (THẤT BẠI) ==
            record.markFailed(eventId);
            repository.save(record);
            
            stateService.failed(eventId, ex.getMessage());
            throw ex;
        }
    }
}
