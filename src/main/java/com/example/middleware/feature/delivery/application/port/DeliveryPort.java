package com.example.middleware.feature.delivery.application.port;

import com.example.middleware.feature.processing.domain.event.TransformedEvent;

public interface DeliveryPort {

    String write(TransformedEvent event);

}