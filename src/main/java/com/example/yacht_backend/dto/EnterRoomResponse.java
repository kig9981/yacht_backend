package com.example.yacht_backend.dto;

public class EnterRoomResponse {
    private boolean isAccepted;
    private String response;

    public EnterRoomResponse() {}

    public EnterRoomResponse(String response, boolean isAccepted) {
        this.response = response;
        this.isAccepted = isAccepted;
    }
    
    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean getIsAccepted() {
        return this.isAccepted;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }
}
