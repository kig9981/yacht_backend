package com.example.yacht_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.yacht_backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserId(String userId);
    List<User> findBySessionId(String sessionId);
}
