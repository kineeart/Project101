package com.example.middleware.feature.delivery.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

import com.example.middleware.feature.delivery.application.port.DeliveryAttemptRepositoryPort;
import com.example.middleware.feature.delivery.domain.DeliveryAttempt;

@Repository
public class InMemoryDeliveryAttemptRepository implements DeliveryAttemptRepositoryPort {

    // Quản lý dữ liệu bằng Map với key là batchId, value là danh sách các lượt thử (attempts) của batch đó
    private final Map<String, List<DeliveryAttempt>> store = new ConcurrentHashMap<>();

    @Override
    public void save(DeliveryAttempt attempt) {
        store.computeIfAbsent(
                attempt.getBatchId(),
                id -> new ArrayList<>()
        ).add(attempt);
    }

    @Override
    public List<DeliveryAttempt> findByBatchId(String batchId) {
        return store.getOrDefault(batchId, List.of());
    }
}
