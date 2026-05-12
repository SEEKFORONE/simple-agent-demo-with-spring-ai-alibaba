package org.example.entity;

public class ChatRequest {
    private String message;
    private String sessionId;

    public String getMessage() {
        return message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
