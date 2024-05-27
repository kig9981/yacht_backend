package com.example.yacht_backend.Entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Room {
    private @Id
    @GeneratedValue Long id;
    private String roomId;
    private String hostUser;
    private String guestUser;

    Room() {}

    Room(UUID roomId, String hostUser, String guestUser) {
        this.roomId = roomId.toString();
        this.hostUser = hostUser;
        this.guestUser = guestUser;
    }
    
    public String getRoomId() {
        return this.roomId;
    }

    public String getHostUser() {
        return this.hostUser;
    }

    public String getGuestUser() {
        return this.guestUser;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setHostUser(String hostUser) {
        this.hostUser = hostUser;
    }

    public void setGuestUser(String guestUser) {
        this.guestUser = guestUser;
    }

}
