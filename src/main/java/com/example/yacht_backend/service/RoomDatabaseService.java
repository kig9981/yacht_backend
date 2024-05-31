package com.example.yacht_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.yacht_backend.repository.RoomRepository;
import com.example.yacht_backend.exception.RoomNotFoundException;
import com.example.yacht_backend.model.Room;

@Service
public class RoomDatabaseService {
    private final RoomRepository roomRepository;

    RoomDatabaseService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly=true)
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Transactional
    public Room save(Room room) {
        return roomRepository.save(room);
    }
    
    @Transactional
    public Room addGuestUserToRoom(String hostUserId, String guestUserId) throws RoomNotFoundException {
        Room hostRoom = findRoomByHostUserId(hostUserId);
        if (hostRoom.getHostUserId() != hostUserId) {
            throw new RoomNotFoundException("invalid room info(host)");
        }
        hostRoom.setGuestUserId(guestUserId);
        return roomRepository.save(hostRoom);
    }

    @Transactional(readOnly=true)
    public Room findRoomByHostUserId(String hostUserId) {
        List<Room> hostRooms = roomRepository.findByHostUserId(hostUserId);
        if (hostRooms.isEmpty()) {
            return null;
        }
        Room hostRoom = hostRooms.get(0); 
        return hostRoom;
    }

    @Transactional(readOnly=true)
    public Room findRoomByGuestUserId(String guestUserId) {
        List<Room> guestRooms = roomRepository.findByGuestUserId(guestUserId);
        if (guestRooms.isEmpty()) {
            return null;
        }
        Room guestRoom = guestRooms.get(0); 
        return guestRoom;
    }

    @Transactional(readOnly=true)
    public Room findRoomById(String roomId) {
        List<Room> rooms = roomRepository.findByRoomId(roomId);
        if (rooms.isEmpty()) {
            return null;
        }
        Room room = rooms.get(0); 
        return room;
    }

    @Transactional(readOnly=true)
    public boolean isUserInRoom(String userId) {
        return findRoomByHostUserId(userId) != null || findRoomByGuestUserId(userId) != null;
    }
}
