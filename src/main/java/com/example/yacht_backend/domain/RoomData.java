package com.example.yacht_backend.domain;

import org.springframework.web.context.request.async.DeferredResult;

public class RoomData {
    private String roomId;
    private String hostUserId;
    private String hostUserData;
    private DeferredResult<String> guestUserId;
    private DeferredResult<String> guestUserData;
    private boolean open;

    public RoomData() {}

    public RoomData(String roomId, String hostUserId, String hostUserData, DeferredResult<String> guestUserId, DeferredResult<String> guestUserData) {
        this.roomId = roomId;
        this.hostUserId = hostUserId;
        this.hostUserData = hostUserData;
        this.guestUserId = guestUserId;
        this.guestUserData = guestUserData;
        open = true;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHostUserId() {
        return hostUserId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public String getHostUserData() {
        return this.hostUserData;
    }

    public void setHostUserData(String hostUserData) {
        this.hostUserData = hostUserData;
    }

    public DeferredResult<String> getGuestUserId() {
        return guestUserId;
    }

    public void setGuestUserId(DeferredResult<String> guestUserId) {
        this.guestUserId = guestUserId;
    }

    public DeferredResult<String> getGuestUserData() {
        return guestUserData;
    }

    public void setGuestUserData(DeferredResult<String> guestUserData) {
        this.guestUserData = guestUserData;
    }

    public void close() {
        open = false;
    }

    public boolean isOpen() {
        return open;
    }
}
