package com.example.middleware.feature.delivery.application.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.middleware.feature.delivery.application.port.DeliveryAttemptRepositoryPort;
import com.example.middleware.feature.delivery.domain.DeliveryAttempt;

@Service
public class DefaultDeliveryRuntimeService implements DeliveryRuntimeService {

    private final DeliveryAttemptRepositoryPort attemptRepository;

    public DefaultDeliveryRuntimeService(DeliveryAttemptRepositoryPort attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    @Override
    public DeliveryAttempt start(String batchId) {
        // Tự động tính toán số lượt thử: Lấy tổng số lượng hiện tại trong kho + 1 [INDEX]
        List<DeliveryAttempt> existingAttempts = attemptRepository.findByBatchId(batchId);
        int nextAttemptNo = existingAttempts.size() + 1;

        // Sinh lượt ghi mới với trạng thái ban đầu là WRITING
        DeliveryAttempt newAttempt = new DeliveryAttempt(batchId, nextAttemptNo);
        attemptRepository.save(newAttempt);
        return newAttempt;
    }

    @Override
    public void success(String batchId) {
        List<DeliveryAttempt> attempts = attemptRepository.findByBatchId(batchId);
        if (!attempts.isEmpty()) {
            // Lấy lượt thử cuối cùng (gần nhất) ra để đóng dấu thành công [INDEX]
            DeliveryAttempt latestAttempt = attempts.get(attempts.size() - 1);
            latestAttempt.markSuccess();
        }
    }

    @Override
    public void failed(String batchId, String error) {
        List<DeliveryAttempt> attempts = attemptRepository.findByBatchId(batchId);
        if (!attempts.isEmpty()) {
            // Lấy lượt thử cuối cùng ra để cập nhật nhãn FAILED kèm message lỗi hệ thống [INDEX]
            DeliveryAttempt latestAttempt = attempts.get(attempts.size() - 1);
            latestAttempt.markFailed(error);
        }
    }
}
