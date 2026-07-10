package com.example.middleware.feature.metadata.infrastructure.repository;

import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.domain.EventMetadata;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMetadataRepository implements MetadataRepository {

    // Khai báo Map lưu trữ dữ liệu giả lập ở cấp độ thuộc tính Class
    private final Map<String, EventMetadata> metadata = new HashMap<>();

    // Sử dụng Constructor để nạp (seed) dữ liệu mẫu khi Bean được khởi tạo
    public InMemoryMetadataRepository() {
        metadata.put(
            "PROFILE_1",
            new EventMetadata(
                "PROFILE_1",
                "HQ_Price_Master"
            )
        );
    }

    @Override
    public EventMetadata getEventMetadata() {
        // Trả về dữ liệu mẫu dựa trên key fixed của bạn
        return metadata.get("PROFILE_1");
    }
}
