package com.example.yacht_backend.dto;

public class WebSocketMessage {
    private String userId;
    private boolean acceptEnter;

    public WebSocketMessage() {}

    public WebSocketMessage(String userId, boolean acceptEnter) {
        this.userId = userId;
        this.acceptEnter = acceptEnter;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getAcceptEnter() {
        return this.acceptEnter;
    }

    public void setAcceptEnter(boolean acceptEnter) {
        this.acceptEnter = acceptEnter;
    }
}
