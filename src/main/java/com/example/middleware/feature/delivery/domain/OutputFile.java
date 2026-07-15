package com.example.middleware.feature.delivery.domain;

import java.util.List;

public record OutputFile(

        String filePath,

        List<String> lines

) {
}