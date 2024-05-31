package com.example.yacht_backend.service;

import org.springframework.stereotype.Service;

import com.example.yacht_backend.repository.UserRepository;

@Service
public class UserService {
    private final UserDatabaseService userDatabaseService;

    UserService(UserDatabaseService userDatabaseService) {
        this.userDatabaseService = userDatabaseService;
    }
}
