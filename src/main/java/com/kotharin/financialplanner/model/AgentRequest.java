package com.kotharin.financialplanner.model;

public class AgentRequest {
    private String conversationId;
    private String request;

    public AgentRequest() {
    }

    public AgentRequest(String conversationId, String request) {
        this.conversationId = conversationId;
        this.request = request;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
