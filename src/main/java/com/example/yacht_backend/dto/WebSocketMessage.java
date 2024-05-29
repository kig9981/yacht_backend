package com.example.yacht_backend.dto;

public class WebSocketMessage {
    private String roomId;
    private String userId;
    private boolean acceptEnter;

    public WebSocketMessage() {}

    public WebSocketMessage(String roomId, String userId, boolean acceptEnter) {
        this.roomId = roomId;
        this.userId = userId;
        this.acceptEnter = acceptEnter;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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
