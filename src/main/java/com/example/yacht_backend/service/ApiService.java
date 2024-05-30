package com.example.yacht_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yacht_backend.exception.RoomNotFoundException;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.websocket.WebSocketHandler;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ApiService {
    private final ApiDatabaseService apiDatabaseService;

    ApiService(ApiDatabaseService apiDatabaseService) {
        this.apiDatabaseService = apiDatabaseService;
    }

    public List<Room> getAllRooms() {
        return apiDatabaseService.findAll();
    }
    
    public String createNewRoom(String userId) {
        Room hostRoom = apiDatabaseService.findRoomByHostUserId(userId);
        if (hostRoom != null) {
            return hostRoom.getRoomId();
        }
        Room guestRoom = apiDatabaseService.findRoomByGuestUserId(userId);
        if (guestRoom != null) {
            return null;
        }
        String roomId = UUID.randomUUID().toString();
        Room newRoom = new Room(roomId, userId, null);
        apiDatabaseService.save(newRoom);
        return roomId.toString();
    }

    public String enterRoom(String roomId, String userId) throws Exception {
        Room room = apiDatabaseService.findRoomById(roomId);
        if (room == null || room.getGuestUserId() != null) {
            return "REJECTED";
        }
        boolean requestSuccess = WebSocketHandler.enterRoom(room.getHostUserId(), userId);
        if (requestSuccess) {
            return "PENDING";
        }
        return "REJECTED";
    }
}
