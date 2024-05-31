package com.example.yacht_backend.dto;


public class EnterRoomRequest {
    private String userId;

    public EnterRoomRequest() {}

    public EnterRoomRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
