package com.example.yacht_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yacht_backend.model.ActiveGames;
import java.util.List;


public interface ActiveGamesRepository extends JpaRepository<ActiveGames, Long> {
    List<ActiveGames> findByRoomId(String roomId);
    List<ActiveGames> findByHostUserId(String hostUser);
    List<ActiveGames> findByGuestUserId(String guestUserId);
}
