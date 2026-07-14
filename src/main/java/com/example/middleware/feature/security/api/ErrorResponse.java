package com.example.middleware.feature.security.api;

import java.time.Instant;

public record ErrorResponse(

        String code,

        String message,

        Instant timestamp

) {
}