package com.example.yacht_backend.dto;


public class EnterRoomRequest {
    private String sessionId;

    public EnterRoomRequest() {}

    public EnterRoomRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
