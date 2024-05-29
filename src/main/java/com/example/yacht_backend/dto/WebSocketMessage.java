package com.example.yacht_backend.dto;

import org.springframework.web.socket.TextMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketMessage {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String hostUserId;
    private String guestUserId;
    private int acceptEnter; // 0: REJECTED, 1: ACCEPTED, 2: TIMEOUT
    public static final int REJECTED = 0;
    public static final int ACCEPTED = 1;
    public static final int TIMEOUT = 2;
    
    public WebSocketMessage() {}

    public WebSocketMessage(String hostUserId, String guestUserId, int acceptEnter) {
        this.hostUserId = hostUserId;
        this.guestUserId = guestUserId;
        this.acceptEnter = acceptEnter;
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

    public int getAcceptEnter() {
        return this.acceptEnter;
    }

    public void setAcceptEnter(int acceptEnter) {
        this.acceptEnter = acceptEnter;
    }

    public TextMessage toTextMessage() throws Exception {
        return new TextMessage(objectMapper.writeValueAsString(this));
    }
}
