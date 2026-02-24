package com.kotharin.financialplanner.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AgentResponse {
    public String conversationId;
    public String message;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public AgentResponse() {
    }

    public AgentResponse(String conversationId, String message) {
        this.conversationId = conversationId;
        this.message = message;
    }

    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"conversationId\":\"" + conversationId + "\", \"message\":\"error processing json\"}";
        }
    }
}
