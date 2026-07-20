package com.example.middleware.feature.ingestion.application;

import com.example.middleware.feature.intake.application.port.EventRepositoryPort;
import com.example.middleware.feature.intake.domain.EventRecord;
import com.example.middleware.feature.orchestration.application.Pipeline;
import com.example.middleware.feature.orchestration.application.builder.PipelineContextBuilder;
import com.example.middleware.feature.orchestration.application.PipelineContext; 
import com.example.middleware.feature.runtime.application.service.RuntimeStateService; // IMPORT dịch vụ lifecycle mới

import org.springframework.stereotype.Service;

@Service
public class ProcessEventUseCase {

    private final EventRepositoryPort repository;
    private final PipelineContextBuilder contextBuilder;
    private final Pipeline pipeline;
    private final RuntimeStateService stateService; // Tinh giản: Chỉ giữ lại service vòng đời tập trung

    // Constructor mới: Gọn gàng, Inject RuntimeStateService từ Spring Boot
    public ProcessEventUseCase(
            EventRepositoryPort repository,
            PipelineContextBuilder contextBuilder,
            Pipeline pipeline,
            RuntimeStateService stateService) {
        this.repository = repository;
        this.contextBuilder = contextBuilder;
        this.pipeline = pipeline;
        this.stateService = stateService;
    }

    public void process(String eventId) {
        // Tìm Event và validate
        EventRecord record = repository.findById(eventId);
        if (record == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }

        // LƯU Ý: Lệnh check tìm BatchRecord thủ công đã được xóa bỏ tại đây, 
        // vì RuntimeStateService bên trong sẽ tự động chịu trách nhiệm tìm kiếm và ném lỗi nếu không thấy Batch.

        try {
            // == BƯỚC 1: PROCESSING ==
            record.markProcessing();
            repository.save(record);
            stateService.processing(eventId); 

            // == BƯỚC 2: VALIDATED ==
            // (Đứng sau khi Worker nhận nhưng trước khi Mapping dữ liệu)
            stateService.validated(eventId);

            // Build PipelineContext từ RawEvent của record
            PipelineContext context = contextBuilder.build(record.getRawEvent());

            // == BƯỚC 3: MAPPED & BUILT ==
            // (Xảy ra ngay khi hàm build dữ liệu chạy xong thành công)
            stateService.mapped(eventId);
            stateService.built(eventId);

            // == BƯỚC 4: WRITING ==
            // (Đánh dấu hệ thống bắt đầu đẩy dữ liệu đi ghi)
            stateService.writing(eventId);

            // Chạy Pipeline (thực thi bước Delivery/Write cuối)
            pipeline.execute(context);

            // == BƯỚC 5: WRITTEN (THÀNH CÔNG) ==
            record.markWritten(context.getFilePath());
            repository.save(record);
            stateService.written(eventId);

        } catch (Exception ex) {
            // == BƯỚC 6: FAILED (THẤT BẠI) ==
            record.markFailed(eventId);
            repository.save(record);

            // Ghi nhận chính xác lỗi sập tại giai đoạn nào kèm message lỗi hệ thống
            stateService.failed(eventId, ex.getMessage());

            throw ex;
        }
    }
}
