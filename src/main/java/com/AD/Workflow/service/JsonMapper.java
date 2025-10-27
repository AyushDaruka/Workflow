package com.AD.Workflow.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JsonMapper {

    private final ObjectMapper objectMapper;

    // Spring injects the configured ObjectMapper
    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String convertObjectToJson(Object object) {
        try {
            // writeValueAsString() performs the serialization
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            // Handle serialization error (e.g., logging or throwing a custom exception)
            throw new RuntimeException("Error converting object to JSON string", e);
        }
    }

    public Map<String, Object> convertJsonStringToObject(String jsonString) {
        try {

            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};

            return objectMapper.readValue(jsonString, typeRef);

        } catch (Exception e) {
            // Handle parsing errors (e.g., malformed JSON)
            throw new RuntimeException("Failed to deserialize JSON string to Map<String, Object>.", e);
        }
    }
}