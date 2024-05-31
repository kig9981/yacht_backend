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
    @UniqueConstraint(columnNames = "hostUserId")
})
public class PendingRoom {
    private @Id
    @GeneratedValue Long id;
    @Column(unique = true)
    private String roomId;
    @Column(unique = true)
    private String hostUserId;
    private String hostUserData;

    @Version
    private Integer version;

    public PendingRoom() {}

    public PendingRoom(String roomId, String hostUserId, String hostUserData) {
        this.roomId = roomId;
        this.hostUserId = hostUserId;
        this.hostUserData = hostUserData;
    }
    
    public String getRoomId() {
        return this.roomId;
    }

    public String getHostUserId() {
        return this.hostUserId;
    }

    public String getHostUserData() {
        return this.hostUserData;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public void setHostData(String hostUserData) {
        this.hostUserData = hostUserData;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
