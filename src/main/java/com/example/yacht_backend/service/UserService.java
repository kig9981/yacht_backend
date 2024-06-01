package com.example.yacht_backend.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.yacht_backend.model.User;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDatabaseService userDatabaseService;

    UserService(UserDatabaseService userDatabaseService) {
        this.userDatabaseService = userDatabaseService;
    }

    public User createUser() {
        logger.info("UserService.createUser 실행");
        String userId = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();
        User user = new User(userId, sessionId);
        return userDatabaseService.save(user);
    }
}
