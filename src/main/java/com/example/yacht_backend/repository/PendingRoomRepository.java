package com.example.yacht_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yacht_backend.model.PendingRoom;
import java.util.List;

public interface PendingRoomRepository extends JpaRepository<PendingRoom, Long> {
    List<PendingRoom> findByRoomId(String roomId);
    List<PendingRoom> findByHostUserId(String hostUser);
}