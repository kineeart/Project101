package com.example.middleware.feature.processing.domain.transformation;

import java.util.Map;

public class XrefTransformer {

    public Object transform(
        Object value,
        Map<String,String> dictionary){

    if(value == null){
        return null;
    }

    if(dictionary == null){
        return value;
    }

    return dictionary.getOrDefault(
            value.toString(),
            value.toString()
    );
}
}