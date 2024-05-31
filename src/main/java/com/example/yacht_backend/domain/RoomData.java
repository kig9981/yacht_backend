package com.example.yacht_backend.domain;

import org.springframework.web.context.request.async.DeferredResult;

import com.example.yacht_backend.dto.CreateNewRoomResponse;

public class RoomData {
    private String roomId;
    private String hostUserId;
    private String hostUserData;
    private DeferredResult<CreateNewRoomResponse> createNewRoomResponse;
    private boolean open;

    public RoomData() {}

    public RoomData(String roomId, String hostUserId, String hostUserData, DeferredResult<CreateNewRoomResponse> createNewRoomResponse) {
        this.roomId = roomId;
        this.hostUserId = hostUserId;
        this.hostUserData = hostUserData;
        this.createNewRoomResponse = createNewRoomResponse;
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

    public DeferredResult<CreateNewRoomResponse> getCreateNewRoomResponse() {
        return createNewRoomResponse;
    }

    public void setCreateNewRoomResponse(DeferredResult<CreateNewRoomResponse> createNewRoomResponse) {
        this.createNewRoomResponse = createNewRoomResponse;
    }

    public void close() {
        open = false;
    }

    public boolean isOpen() {
        return open;
    }
}
