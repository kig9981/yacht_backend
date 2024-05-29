package com.example.yacht_backend.dto;


public class EnterRoomRequest {
    private String roomId;

    public EnterRoomRequest() {}

    public EnterRoomRequest(String roomId) {
        this.roomId = roomId;
    }
    
    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
