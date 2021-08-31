package com.crypto.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static String toString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); //use log instead of printStackTrace here
        }
        return null;
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return objectMapper.readerFor(clazz).readValue(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();// use log instead of printStackTrace here
        }
        return null;
    }
}
