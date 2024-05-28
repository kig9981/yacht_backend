package com.example.yacht_backend.dto;

public class CreateNewRoomResponse {
    private String roomId;

    CreateNewRoomResponse() {}

    CreateNewRoomResponse(String roomId) {
        this.roomId = roomId;
    }

    String getRoomId() {
        return this.roomId;
    }

    void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
