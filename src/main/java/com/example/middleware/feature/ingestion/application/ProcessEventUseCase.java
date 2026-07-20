package com.example.middleware.feature.ingestion.application;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.builder.PipelineContextBuilder;
import com.example.middleware.feature.orchestration.application.PipelineContext; 
import com.example.middleware.feature.runtime.application.service.RuntimeStateService;
import com.example.middleware.feature.runtime.application.port.BatchRepositoryPort;
import com.example.middleware.feature.runtime.domain.BatchRecord;
import com.example.middleware.feature.runtime.domain.batch.BatchStatus;
import com.example.middleware.feature.delivery.application.service.DeliveryRuntimeService; // THÊM IMPORT NÀY

import org.springframework.stereotype.Service;

@Service
public class ProcessEventUseCase {

    private final EventRepositoryPort repository;
    private final PipelineContextBuilder contextBuilder;
    private final Pipeline pipeline;
    private final RuntimeStateService stateService;
    private final BatchRepositoryPort batchRepository;
    private final DeliveryRuntimeService deliveryRuntime; // 1. INJECT THÊM BIẾN NÀY

    public ProcessEventUseCase(
            EventRepositoryPort repository,
            PipelineContextBuilder contextBuilder,
            Pipeline pipeline,
            RuntimeStateService stateService,
            BatchRepositoryPort batchRepository,
            DeliveryRuntimeService deliveryRuntime) { // 2. Nhận thêm vào Constructor
        this.repository = repository;
        this.contextBuilder = contextBuilder;
        this.pipeline = pipeline;
        this.stateService = stateService;
        this.batchRepository = batchRepository;
        this.deliveryRuntime = deliveryRuntime;
    }

    public void process(String eventId) {
        EventRecord record = repository.findById(eventId);
        if (record == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }

        BatchRecord batch = batchRepository.findById(eventId);
        if (batch == null) {
            throw new IllegalArgumentException("Batch not found: " + eventId);
        }

        if (batch.getStatus() != BatchStatus.PROCESSING) {
            throw new IllegalStateException("Batch has not been claimed: " + eventId);
        }

        try {
            record.markProcessing();
            repository.save(record);

            // GIAI ĐOẠN 2: VALIDATION STAGE
            stateService.validated(eventId); 

            // GIAI ĐOẠN 3: MAPPING STAGE
            PipelineContext context = contextBuilder.build(record.getRawEvent());
            stateService.mapped(eventId); 

            // GIAI ĐOẠN 4: BUILD STAGE
            stateService.built(eventId); 

            // =========================================================
            // ĐỒNG BỘ DELIVERY RUNTIME CHO BƯỚC GHI FILE CỐT LÕI
            // =========================================================

            // GIAI ĐOẠN 5: WRITE STAGE (BẤT ĐẦU GHI)
            stateService.writing(eventId); 
            deliveryRuntime.start(eventId); // Kích hoạt: Tạo một Lượt thử ghi mới mang nhãn WRITING [INDEX]

            // Thực thi lệnh Delivery (Ghi file CSV xuống đĩa cứng)
            pipeline.execute(context);

            // GHI FILE THÀNH CÔNG: Đóng dấu thành công cho riêng lượt Delivery đó trước [INDEX]
            deliveryRuntime.success(eventId); 

            // GIAI ĐOẠN 6: WRITTEN (HOÀN THÀNH TOÀN BỘ VÒNG ĐỜI BATCH CHÍNH)
            record.markWritten(context.getFilePath());
            repository.save(record);
            stateService.written(eventId); 

        } catch (Exception ex) {
            // GHI FILE THẤT BẠI: Đóng dấu thất bại cho lượt Delivery hiện tại kèm thông báo lỗi chi tiết [INDEX]
            deliveryRuntime.failed(eventId, ex.getMessage());

            // GIAI ĐOẠN THẤT BẠI TỔNG THỂ CỦA BATCH
            record.markFailed(eventId);
            repository.save(record);
            stateService.failed(eventId, ex.getMessage()); 
            
            throw ex;
        }
    }
}
