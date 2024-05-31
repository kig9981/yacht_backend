package com.example.yacht_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yacht_backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
