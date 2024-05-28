package com.example.yacht_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.repository.RoomRepository;

@Service
public class ApiService {
    private final RoomRepository roomRepository;

    ApiService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    public String createNewRoom(String userId) {
        UUID roomId = UUID.randomUUID();
        Room newRoom = new Room(roomId, userId, null);
        roomRepository.save(newRoom);
        return roomId.toString();
    }
}
