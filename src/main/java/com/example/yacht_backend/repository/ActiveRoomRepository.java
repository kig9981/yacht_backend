package com.example.yacht_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yacht_backend.model.ActiveRoom;
import java.util.List;


public interface ActiveRoomRepository extends JpaRepository<ActiveRoom, Long> {
    List<ActiveRoom> findByRoomId(String roomId);
    List<ActiveRoom> findByHostUserId(String hostUser);
    List<ActiveRoom> findByGuestUserId(String guestUserId);
}
