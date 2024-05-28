package com.example.yacht_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yacht_backend.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    
}
