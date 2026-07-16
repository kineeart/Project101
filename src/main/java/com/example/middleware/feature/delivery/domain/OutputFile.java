package com.example.middleware.feature.delivery.domain;

import java.util.List;

public record OutputFile(

    String fileName,

    List<String> lines

) {
}