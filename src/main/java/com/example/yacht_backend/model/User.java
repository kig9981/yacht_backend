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
    @UniqueConstraint(columnNames = "userId"),
    @UniqueConstraint(columnNames = "sessionId")
})
public class User {
    private @Id
    @GeneratedValue Long id;
    @Column(unique = true)
    private String userId;
    @Column(unique = true)
    private String sessionId;

    @Version
    private Integer version;

    public User() {}

    public User(String userId, String sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }
    
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
