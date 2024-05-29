package com.example.yacht_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.repository.RoomRepository;
import com.example.yacht_backend.websocket.WebSocketHandler;


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
        String roomId = UUID.randomUUID().toString();
        Room newRoom = new Room(roomId, userId, null);
        roomRepository.save(newRoom);
        return roomId.toString();
    }

    public String enterRoom(String roomId, String userId) throws Exception {
        List<Room> rooms = roomRepository.findByRoomId(roomId);
        if (rooms.size() != 1) {
            throw new Exception("invalid db(multiple roomId)");
        }
        Room room = rooms.get(0);
        if (room.getGuestUserId() != null) {
            return "REJECTED";
        }
        boolean requestSuccess = WebSocketHandler.enterRoom(room.getHostUserId(), userId);
        if (requestSuccess) {
            return "PENDING";
        }
        return "REJECTED";
    }

    @Transactional
    public void addGuestUserToRoom(String hostUserId, String guestUserId) throws Exception {
        List<Room> hostRooms = roomRepository.findByHostUserId(hostUserId);
        if (hostRooms.size() != 1) {
            throw new Exception("invalid room info(multiple rooms)");
        }
        Room hostRoom = hostRooms.get(0);
        if (hostRoom.getHostUserId() != hostUserId) {
            throw new Exception("invalid room info(host)");
        }
        hostRoom.setGuestUserId(guestUserId);
        roomRepository.save(hostRoom);
    }
}
