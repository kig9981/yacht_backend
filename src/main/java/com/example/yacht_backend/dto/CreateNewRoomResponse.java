package com.example.yacht_backend.dto;

import org.springframework.web.context.request.async.DeferredResult;

public class CreateNewRoomResponse {
    private String roomId;
    private DeferredResult<String> guestUserId;
    private DeferredResult<String> data;

    public CreateNewRoomResponse() {}

    public CreateNewRoomResponse(String roomId, DeferredResult<String> guestUserId, DeferredResult<String> data) {
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

    public DeferredResult<String> getGuestUserId() {
        return this.guestUserId;
    }

    public void setGuestUserId(DeferredResult<String> guestUserId) {
        this.guestUserId = guestUserId;
    }

    public DeferredResult<String> getData() {
        return this.data;
    }

    public void setData(DeferredResult<String> data) {
        this.data = data;
    }
}
