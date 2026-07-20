package com.example.middleware.feature.delivery.application.service;

import com.example.middleware.feature.delivery.domain.DeliveryAttempt;

public interface DeliveryRuntimeService {
    DeliveryAttempt start(String batchId);
    void success(String batchId);
    void failed(String batchId, String error);
}
