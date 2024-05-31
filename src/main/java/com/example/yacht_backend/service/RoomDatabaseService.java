package com.example.yacht_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.yacht_backend.repository.ActiveGamesRepository;
import com.example.yacht_backend.exception.RoomNotFoundException;
import com.example.yacht_backend.model.ActiveGames;

@Service
public class RoomDatabaseService {
    private final ActiveGamesRepository roomRepository;

    RoomDatabaseService(ActiveGamesRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly=true)
    public List<ActiveGames> findAll() {
        return roomRepository.findAll();
    }

    @Transactional
    public ActiveGames save(ActiveGames room) {
        return roomRepository.save(room);
    }
    
    @Transactional
    public ActiveGames addGuestUserToRoom(String hostUserId, String guestUserId) throws RoomNotFoundException {
        ActiveGames hostRoom = findRoomByHostUserId(hostUserId);
        if (hostRoom.getHostUserId() != hostUserId) {
            throw new RoomNotFoundException("invalid room info(host)");
        }
        hostRoom.setGuestUserId(guestUserId);
        return roomRepository.save(hostRoom);
    }

    @Transactional(readOnly=true)
    public ActiveGames findRoomByHostUserId(String hostUserId) {
        if (hostUserId == null) {
            return null;
        }
        List<ActiveGames> hostRooms = roomRepository.findByHostUserId(hostUserId);
        if (hostRooms.isEmpty()) {
            return null;
        }
        ActiveGames hostRoom = hostRooms.get(0); 
        return hostRoom;
    }

    @Transactional(readOnly=true)
    public ActiveGames findRoomByGuestUserId(String guestUserId) {
        if (guestUserId == null) {
            return null;
        }
        List<ActiveGames> guestRooms = roomRepository.findByGuestUserId(guestUserId);
        if (guestRooms.isEmpty()) {
            return null;
        }
        ActiveGames guestRoom = guestRooms.get(0); 
        return guestRoom;
    }

    @Transactional(readOnly=true)
    public ActiveGames findRoomById(String roomId) {
        if (roomId == null) {
            return null;
        }
        List<ActiveGames> rooms = roomRepository.findByRoomId(roomId);
        if (rooms.isEmpty()) {
            return null;
        }
        ActiveGames room = rooms.get(0); 
        return room;
    }

    @Transactional(readOnly=true)
    public boolean isUserInRoom(String userId) {
        return findRoomByHostUserId(userId) != null || findRoomByGuestUserId(userId) != null;
    }
}
