package com.example.yacht_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(name = "id_uniqueness_check", uniqueConstraints = {
    @UniqueConstraint(columnNames = "roomId"),
    @UniqueConstraint(columnNames = {"hostUserId", "guestUserId"})
})
public class ActiveGames {
    private @Id
    @GeneratedValue Long id;
    @Column(unique = true)
    private String roomId;
    @Column(unique = true)
    private String hostUserId;
    @Column(unique = true)
    private String guestUserId;

    @Version
    private Integer version;

    public ActiveGames() {}

    public ActiveGames(String roomId, String hostUserId, String guestUserId) {
        this.roomId = roomId;
        this.hostUserId = hostUserId;
        this.guestUserId = guestUserId;
    }
    
    public String getRoomId() {
        return this.roomId;
    }

    public String getHostUserId() {
        return this.hostUserId;
    }

    public String getGuestUserId() {
        return this.guestUserId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public void setGuestUserId(String guestUserId) {
        this.guestUserId = guestUserId;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
