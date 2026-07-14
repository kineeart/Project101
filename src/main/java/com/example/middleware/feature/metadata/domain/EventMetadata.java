package com.example.middleware.feature.metadata.domain;

import java.util.List;

public class EventMetadata {

    private final String profileId;
    private final String sourceSystem;
    private final List<TableRule> tableRules; // 1. Bổ sung List<TableRule> theo yêu cầu

    // 2. Định nghĩa Constructor đúng chuẩn với đầy đủ thuộc tính
    public EventMetadata(
            String profileId, 
            String sourceSystem, 
            List<TableRule> tableRules) {
        
        this.profileId = profileId;
        this.sourceSystem = sourceSystem;
        this.tableRules = tableRules != null ? List.copyOf(tableRules) : List.of(); // Bảo vệ tính toàn vẹn của danh sách
    }

    // 3. Chỉ giữ lại các hàm Getter, không dùng Setter giả
    public String getProfileId() {
        return profileId;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public List<TableRule> getTableRules() {
        return tableRules;
    }
}
