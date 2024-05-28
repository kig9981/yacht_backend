package com.example.yacht_backend;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ApiService {
    private final RoomRepository roomRepository;

    ApiService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
