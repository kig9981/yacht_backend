package com.example.yacht_backend.dto;

import org.springframework.web.context.request.async.DeferredResult;

public class CreateNewRoomResponse {
    private String roomId;
    private DeferredResult<String> guestId;

    public CreateNewRoomResponse() {}

    public CreateNewRoomResponse(String roomId, DeferredResult<String> guestId) {
        this.roomId = roomId;
        this.guestId = guestId;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public DeferredResult<String> getGuestId() {
        return this.guestId;
    }

    public void setGuestId(DeferredResult<String> guestId) {
        this.guestId = guestId;
    }
}
