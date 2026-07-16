package com.example.middleware.feature.delivery.application.usecase;

import java.nio.file.Path;
import org.springframework.stereotype.Service;

import com.example.middleware.feature.delivery.application.port.DeliveryArtifactRepository;
import com.example.middleware.feature.delivery.application.port.DeliveryPort;
import com.example.middleware.feature.delivery.application.port.OutputFileWriter;
import com.example.middleware.feature.delivery.application.port.OutputPublisher;
import com.example.middleware.feature.delivery.application.port.WorkspaceManager;
import com.example.middleware.feature.delivery.domain.DeliveryArtifact;
import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.orchestration.application.PipelineContext;
import com.example.middleware.feature.orchestration.application.StageResult;
import com.example.middleware.feature.processing.application.port.RetryPort;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

@Service
public class DefaultDeliveryUseCase implements DeliveryUseCase {

    private final DeliveryPort deliveryPort;
    private final WorkspaceManager workspaceManager;
    private final OutputFileWriter outputFileWriter;
    private final OutputPublisher outputPublisher;
    private final RetryPort retryPort;
    private final DeliveryArtifactRepository artifactRepository;    

    // Inject đầy đủ 3 hạ tầng độc lập theo kiến trúc P7
    public DefaultDeliveryUseCase(
            DeliveryPort deliveryPort,
            WorkspaceManager workspaceManager,
            OutputFileWriter outputFileWriter,
            OutputPublisher outputPublisher,
            RetryPort retryPort,
            DeliveryArtifactRepository artifactRepository) {
        this.deliveryPort = deliveryPort;
        this.workspaceManager = workspaceManager;
        this.outputFileWriter = outputFileWriter;
        this.outputPublisher = outputPublisher;
        this.retryPort = retryPort;
        this.artifactRepository = artifactRepository;
    }

    @Override
    public StageResult deliver(PipelineContext context) {
        TransformedEvent transformed = context.getTransformedEvent();
        String eventId = context.getRawEvent().getEventId();
        DeliveryProfile profile = context.getMappingContext().getDeliveryProfile();

        // 1. Khởi tạo DeliveryArtifact một lần duy nhất ngay từ đầu tiến trình
        DeliveryArtifact artifact = new DeliveryArtifact(eventId);

        try {
            // Bước A: Build dữ liệu bộ nhớ và sinh tên file (Không có IO, không cần retry)
            OutputFile outputFile = deliveryPort.build(transformed, profile);
            
            // Cập nhật tên file vào artifact ngay sau khi build thành công
            artifact.assignFile(outputFile.fileName());

            // Bước B: Phân giải đường dẫn và ghi file vật lý vào Workspace tạm thời
            outputFileWriter.write(outputFile);
            Path workspaceFile = workspaceManager.workspaceFile(outputFile);

            // Bước C: Đẩy file sang thư mục đích Inbound. 
            // ĐÚNG THEO TECHNICAL DESIGN: Chỉ bọc retry cho duy nhất hành động IO/Publish này!
            retryPort.execute(
                    eventId,
                    transformed.getPayload(),
                    () -> {
                        outputPublisher.publish(workspaceFile, profile);
                        return null; // Trả về null cho khớp với Functional Interface RetryAction<T>
                    }
            );

            // 2. KỊCH BẢN THÀNH CÔNG: Đổi trạng thái -> Lưu DB duy nhất 1 lần
            artifact.markPublished();
            artifactRepository.save(artifact);

            // Lưu tên file vào context cho các stage sau
            context.setFilePath(outputFile.fileName());

            return StageResult.SUCCESS;

        } catch (Exception ex) {
            // 3. KỊCH BẢN THẤT BẠI: Sử dụng lại đúng artifact ban đầu -> Đổi trạng thái -> Lưu DB duy nhất 1 lần
            artifact.markFailed();
            artifactRepository.save(artifact);

            // Ném ngược Exception ra ngoài để hệ thống pipeline nhận diện đúng lỗi
            throw ex;
        }
    }
}
