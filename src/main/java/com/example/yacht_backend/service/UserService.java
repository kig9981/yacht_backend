package com.example.yacht_backend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.yacht_backend.model.User;

@Service
public class UserService {
    private final UserDatabaseService userDatabaseService;

    UserService(UserDatabaseService userDatabaseService) {
        this.userDatabaseService = userDatabaseService;
    }

    public User createUser() {
        String userId = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();
        User user = new User(userId, sessionId);
        return userDatabaseService.save(user);
    }
}
