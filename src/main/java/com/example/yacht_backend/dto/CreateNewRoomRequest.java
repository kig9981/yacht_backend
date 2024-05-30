package com.example.yacht_backend.dto;

public class CreateNewRoomRequest {
    private String userId;

    public CreateNewRoomRequest() {}

    public CreateNewRoomRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
