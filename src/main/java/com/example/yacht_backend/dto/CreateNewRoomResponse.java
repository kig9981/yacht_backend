package com.example.yacht_backend.dto;

public class CreateNewRoomResponse {
    private String roomId;
    private String guestUserId;
    private String data;

    public CreateNewRoomResponse() {}

    public CreateNewRoomResponse(String roomId, String guestUserId, String data) {
        this.roomId = roomId;
        this.guestUserId = guestUserId;
        this.data = data;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getGuestUserId() {
        return this.guestUserId;
    }

    public void setGuestUserId(String guestUserId) {
        this.guestUserId = guestUserId;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
