package com.six.homework.supertrader.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
    public static String toPlainJson(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            // If this happens we can't recover anyway
            throw new RuntimeException(ex);
        }
    }
}
