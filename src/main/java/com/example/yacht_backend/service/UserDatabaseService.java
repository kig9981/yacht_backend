package com.example.yacht_backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yacht_backend.model.User;
import com.example.yacht_backend.repository.UserRepository;

@Service
public class UserDatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(UserDatabaseService.class);
    private final UserRepository userRepository;

    UserDatabaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User save(User user) {
        logger.info("UserDatabaseService.save 실행");
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public String findUserIdBySessionId(String sessionId) {
        logger.info("UserDatabaseService.findUserIdBySessionId 실행");
        if (sessionId == null) {
            return null;
        }
        List<User> user = userRepository.findBySessionId(sessionId);
        if (user.isEmpty()) {
            return null;
        }
        return user.get(0).getUserId();
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        logger.info("UserDatabaseService.findAll 실행");
        return userRepository.findAll();
    }
}
