package io.j0a0m4.otp.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperUtils {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

}
