package com.example.middleware.feature.delivery.application.port;

import java.util.List;
import com.example.middleware.feature.delivery.domain.DeliveryAttempt;

public interface DeliveryAttemptRepositoryPort {
    void save(DeliveryAttempt attempt);
    List<DeliveryAttempt> findByBatchId(String batchId);
}
