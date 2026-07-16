package com.example.middleware.feature.delivery.application.port;

import com.example.middleware.feature.delivery.domain.OutputFile;
import com.example.middleware.feature.metadata.domain.DeliveryProfile;
import com.example.middleware.feature.processing.domain.event.TransformedEvent;

public interface DeliveryPort {

    OutputFile build(
            TransformedEvent event,
            DeliveryProfile profile
    );

}