package com.example.yacht_backend.dto;

public class CreateNewRoomResponse {
    private String roomId;

    public CreateNewRoomResponse() {}

    public CreateNewRoomResponse(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
