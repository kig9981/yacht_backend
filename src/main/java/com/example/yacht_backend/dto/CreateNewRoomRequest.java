package com.example.yacht_backend.dto;

public class CreateNewRoomRequest {
    private String userId;

    CreateNewRoomRequest() {}

    CreateNewRoomRequest(String userId) {
        this.userId = userId;
    }

    String getUserId() {
        return this.userId;
    }

    void setUserId(String userId) {
        this.userId = userId;
    }
}
