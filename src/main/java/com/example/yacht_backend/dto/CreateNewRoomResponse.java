package com.example.yacht_backend.dto;

import org.springframework.web.context.request.async.DeferredResult;

public class CreateNewRoomResponse {
    private String roomId;
    private DeferredResult<String> guestUserId;

    public CreateNewRoomResponse() {}

    public CreateNewRoomResponse(String roomId, DeferredResult<String> guestUserId) {
        this.roomId = roomId;
        this.guestUserId = guestUserId;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public DeferredResult<String> getGuestUserId() {
        return this.guestUserId;
    }

    public void setGuestUserId(DeferredResult<String> guestUserId) {
        this.guestUserId = guestUserId;
    }
}
