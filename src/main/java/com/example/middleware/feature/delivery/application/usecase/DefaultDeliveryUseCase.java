package com.example.middleware.feature.delivery.application.usecase;

import org.springframework.stereotype.Service;

import com.example.middleware.feature.delivery.application.port.DeliveryPort;
import com.example.middleware.feature.delivery.domain.DeliveryResult;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.processing.application.port.RetryPort;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;
// Bổ sung import interface DeliveryUseCase của bạn tại đây nếu nó nằm ở package khác
// import com.example.middleware.feature.delivery.application.usecase.DeliveryUseCase;

@Service
public class DefaultDeliveryUseCase implements DeliveryUseCase {

    private final DeliveryPort deliveryPort;
    private final RetryPort retryPort;

    public DefaultDeliveryUseCase(
            DeliveryPort deliveryPort,
            RetryPort retryPort) {
        this.deliveryPort = deliveryPort;
        this.retryPort = retryPort;
    }

    @Override
    public StageResult deliver(PipelineContext context) {

        TransformedEvent transformed = context.getTransformedEvent();
        String eventId = context.getRawEvent().getEventId();

        // Gọi qua RetryPort bọc ngoài cùng logic gửi nhận dữ liệu
       DeliveryResult deliveryResult = retryPort.execute(
        eventId,
        transformed.getPayload(),
        () -> deliveryPort.write(
                transformed,
                context.getMappingContext().getDeliveryProfile()
        )
);

        // Lưu thông tin file đã tạo vào Pipeline Context phục vụ các Stage phía sau
        context.setFilePath(deliveryResult.fileName());

        return StageResult.SUCCESS;
    }
}
