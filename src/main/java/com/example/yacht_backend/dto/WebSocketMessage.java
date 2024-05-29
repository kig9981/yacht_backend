package com.example.yacht_backend.dto;

public class WebSocketMessage {
    private String roomId;
    private String hostUserId;
    private String guestUserId;
    private boolean acceptEnter;

    public WebSocketMessage() {}

    public WebSocketMessage(String roomId, String hostUserId, String guestUserId) {
        this.roomId = roomId;
        this.hostUserId = hostUserId;
        this.guestUserId = guestUserId;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHostUserId() {
        return this.hostUserId;
    }

    public String getGuestUserId() {
        return this.guestUserId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public void setGuestUserId(String guestUserId) {
        this.guestUserId = guestUserId;
    }

    public boolean getAcceptEnter() {
        return this.acceptEnter;
    }

    public void setAcceptEnter(boolean acceptEnter) {
        this.acceptEnter = acceptEnter;
    }
}
