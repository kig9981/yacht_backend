package com.example.yacht_backend.dto;

public class CreateNewRoomRequest {
    private String sessionId;

    public CreateNewRoomRequest() {}

    public CreateNewRoomRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
