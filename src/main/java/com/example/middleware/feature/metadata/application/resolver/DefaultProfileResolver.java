package com.example.middleware.feature.metadata.application.resolver;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultProfileResolver implements ProfileResolver {

    @Override
    public String resolve(Map<String, Object> request) {

        // Phase hiện tại:
        // Chưa có Metadata Rule

        return "PROFILE_1";
    }
}