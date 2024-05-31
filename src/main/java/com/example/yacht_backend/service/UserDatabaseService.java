package com.example.yacht_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yacht_backend.model.User;
import com.example.yacht_backend.repository.UserRepository;

@Service
public class UserDatabaseService {
    private final UserRepository userRepository;

    UserDatabaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public String findUserIdBySessionId(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        List<User> user = userRepository.findBySessionId(sessionId);
        if (user.isEmpty()) {
            return null;
        }
        return user.get(0).getUserId();
    }
}
