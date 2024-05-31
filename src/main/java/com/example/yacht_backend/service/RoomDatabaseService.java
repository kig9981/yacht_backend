package com.example.yacht_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.yacht_backend.repository.ActiveRoomRepository;
import com.example.yacht_backend.exception.RoomNotFoundException;
import com.example.yacht_backend.model.ActiveRoom;

@Service
public class RoomDatabaseService {
    private final ActiveRoomRepository roomRepository;

    RoomDatabaseService(ActiveRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly=true)
    public List<ActiveRoom> findAll() {
        return roomRepository.findAll();
    }

    @Transactional
    public ActiveRoom save(ActiveRoom room) {
        return roomRepository.save(room);
    }
    
    @Transactional
    public ActiveRoom addGuestUserToRoom(String hostUserId, String guestUserId) throws RoomNotFoundException {
        ActiveRoom hostRoom = findRoomByHostUserId(hostUserId);
        if (hostRoom.getHostUserId() != hostUserId) {
            throw new RoomNotFoundException("invalid room info(host)");
        }
        hostRoom.setGuestUserId(guestUserId);
        return roomRepository.save(hostRoom);
    }

    @Transactional(readOnly=true)
    public ActiveRoom findRoomByHostUserId(String hostUserId) {
        if (hostUserId == null) {
            return null;
        }
        List<ActiveRoom> hostRooms = roomRepository.findByHostUserId(hostUserId);
        if (hostRooms.isEmpty()) {
            return null;
        }
        ActiveRoom hostRoom = hostRooms.get(0); 
        return hostRoom;
    }

    @Transactional(readOnly=true)
    public ActiveRoom findRoomByGuestUserId(String guestUserId) {
        if (guestUserId == null) {
            return null;
        }
        List<ActiveRoom> guestRooms = roomRepository.findByGuestUserId(guestUserId);
        if (guestRooms.isEmpty()) {
            return null;
        }
        ActiveRoom guestRoom = guestRooms.get(0); 
        return guestRoom;
    }

    @Transactional(readOnly=true)
    public ActiveRoom findRoomById(String roomId) {
        if (roomId == null) {
            return null;
        }
        List<ActiveRoom> rooms = roomRepository.findByRoomId(roomId);
        if (rooms.isEmpty()) {
            return null;
        }
        ActiveRoom room = rooms.get(0); 
        return room;
    }

    @Transactional(readOnly=true)
    public boolean isUserInRoom(String userId) {
        return findRoomByHostUserId(userId) != null || findRoomByGuestUserId(userId) != null;
    }
}
