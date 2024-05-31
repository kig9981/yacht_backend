package com.example.yacht_backend.dto;


public class EnterRoomRequest {
    private String sessionId;
    private String data;

    public EnterRoomRequest() {}

    public EnterRoomRequest(String sessionId, String data) {
        this.sessionId = sessionId;
        this.data = data;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
