package com.example.middleware.feature.delivery.application.usecase;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.delivery.application.port.DeliveryArtifactRepository;
import com.example.middleware.feature.delivery.application.port.DeliveryPort;
import com.example.middleware.feature.delivery.domain.DeliveryArtifact;
import com.example.middleware.feature.delivery.domain.DeliveryResult;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.processing.application.port.RetryPort;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

@Service
public class DefaultDeliveryUseCase implements DeliveryUseCase {

    private final DeliveryPort deliveryPort;
    private final RetryPort retryPort;
    private final DeliveryArtifactRepository artifactRepository;    

    public DefaultDeliveryUseCase(
            DeliveryPort deliveryPort,
            RetryPort retryPort,
            DeliveryArtifactRepository artifactRepository) {
        this.deliveryPort = deliveryPort;
        this.retryPort = retryPort;
        this.artifactRepository = artifactRepository;
    }

    @Override
    public StageResult deliver(PipelineContext context) {
        TransformedEvent transformed = context.getTransformedEvent();
        String eventId = context.getRawEvent().getEventId();

        // 1. Tạo DeliveryArtifact một lần duy nhất ngay từ đầu tiến trình
        DeliveryArtifact artifact = new DeliveryArtifact(eventId);

        try {
            // 2. Thực thi luồng xử lý qua RetryPort điều phối DeliveryPort
            DeliveryResult deliveryResult = retryPort.execute(
                    eventId,
                    transformed.getPayload(),
                    () -> deliveryPort.write( // Lưu ý đổi thành send() nếu cấu trúc tổng quan Port đã đồng bộ P7
                            transformed,
                            context.getMappingContext().getDeliveryProfile()
                    )
            );

            // 3. Xử lý kịch bản THÀNH CÔNG: Gán tên file -> Đổi trạng thái -> Lưu DB duy nhất 1 lần
            artifact.assignFile(deliveryResult.fileName());
            artifact.markPublished();
            artifactRepository.save(artifact);

            // Cập nhật đường dẫn file vào pipeline context phục vụ các bước sau
            context.setFilePath(deliveryResult.fileName());

            return StageResult.SUCCESS;

        } catch (Exception ex) {
            // 4. Xử lý kịch bản THẤT BẠI: Sử dụng lại đúng artifact ban đầu -> Đổi trạng thái -> Lưu DB duy nhất 1 lần
            artifact.markFailed();
            artifactRepository.save(artifact);

            // Ném ngược Exception ra ngoài để hệ thống/pipeline cha nhận diện lỗi đúng thiết kế
            throw ex;
        }
    }
}
