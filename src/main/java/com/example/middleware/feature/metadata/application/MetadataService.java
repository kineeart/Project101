package com.example.middleware.feature.metadata.application;

import com.example.middleware.feature.metadata.application.port.MetadataRepository;
import com.example.middleware.feature.metadata.domain.EventMetadata;
import com.example.middleware.feature.metadata.domain.FieldRule;
import com.example.middleware.feature.metadata.domain.TableRule;
import com.example.middleware.feature.processing.domain.context.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class MetadataService {

    private final MetadataRepository metadataRepository;

    public MetadataService(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    public MappingContext loadMappingContext(String profileId) {
        // Khởi tạo đối tượng ngữ cảnh mapping
        MappingContext context = new MappingContext();

        // Khởi tạo cấu hình luật cho bảng (TableRule)
        TableRule rule = new TableRule();
        rule.setSourceTable("HQ_Price_Master");
        rule.setTargetTable("MNT");

        // Cấu hình luật cho field thứ nhất: itemId -> ITEM
        FieldRule item = new FieldRule();
        item.setSourceField("itemId");
        item.setTargetField("ITEM");

        // Cấu hình luật cho field thứ hai: price -> PRICE
        FieldRule price = new FieldRule();
        price.setSourceField("price");
        price.setTargetField("PRICE");

        // Thêm các luật trường dữ liệu vào luật bảng
        rule.getFieldRules().add(item);
        rule.getFieldRules().add(price);

        // Đăng ký luật bảng vào MappingContext dựa trên tên bảng nguồn
        context.addRule(
            rule.getSourceTable(),
            rule
        );

        return context;
    }

    public EventMetadata resolveEventMetadata(String profileId) {
        return metadataRepository.getEventMetadata(profileId);
    }
}
