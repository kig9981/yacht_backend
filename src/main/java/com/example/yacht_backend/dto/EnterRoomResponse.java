package com.example.yacht_backend.dto;

public class EnterRoomResponse {
    private String response;

    public EnterRoomResponse() {}

    public EnterRoomResponse(String response) {
        this.response = response;
    }
    
    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
