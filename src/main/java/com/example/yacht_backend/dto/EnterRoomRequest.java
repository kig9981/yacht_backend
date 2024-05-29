package com.example.yacht_backend.dto;


public class EnterRoomRequest {
    private String roomId;
    private String userId;

    public EnterRoomRequest() {}

    public EnterRoomRequest(String roomId, String userId) {
        this.roomId = roomId;
        this.userId = userId;
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
}
