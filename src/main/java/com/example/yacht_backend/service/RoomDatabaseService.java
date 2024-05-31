package com.example.yacht_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.yacht_backend.repository.ActiveRoomRepository;
import com.example.yacht_backend.repository.PendingRoomRepository;
import com.example.yacht_backend.exception.RoomNotFoundException;
import com.example.yacht_backend.model.ActiveRoom;
import com.example.yacht_backend.model.PendingRoom;

@Service
public class RoomDatabaseService {
    private final ActiveRoomRepository activeRoomRepository;
    private final PendingRoomRepository pendingRoomRepository;

    RoomDatabaseService(ActiveRoomRepository activeRoomRepository, PendingRoomRepository pendingRoomRepository) {
        this.activeRoomRepository = activeRoomRepository;
        this.pendingRoomRepository = pendingRoomRepository;
    }

    @Transactional(readOnly=true)
    public List<PendingRoom> findAllPendingRoom() {
        return pendingRoomRepository.findAll();
    }

    @Transactional
    public ActiveRoom save(ActiveRoom room) {
        return activeRoomRepository.save(room);
    }

    @Transactional
    public PendingRoom save(PendingRoom room) {
        return pendingRoomRepository.save(room);
    }

    @Transactional
    public void delete(ActiveRoom room) {
        activeRoomRepository.delete(room);
    }

    @Transactional
    public void delete(PendingRoom room) {
        pendingRoomRepository.delete(room);
    }
    
    @Transactional
    public ActiveRoom addGuestUserToRoom(String hostUserId, String guestUserId) throws RoomNotFoundException {
        ActiveRoom hostRoom = findActiveRoomByHostUserId(hostUserId);
        if (hostRoom.getHostUserId() != hostUserId) {
            throw new RoomNotFoundException("invalid room info(host)");
        }
        hostRoom.setGuestUserId(guestUserId);
        return activeRoomRepository.save(hostRoom);
    }

    @Transactional(readOnly=true)
    public ActiveRoom findActiveRoomByHostUserId(String hostUserId) {
        if (hostUserId == null) {
            return null;
        }
        List<ActiveRoom> hostRooms = activeRoomRepository.findByHostUserId(hostUserId);
        if (hostRooms.isEmpty()) {
            return null;
        }
        ActiveRoom hostRoom = hostRooms.get(0); 
        return hostRoom;
    }

    @Transactional(readOnly=true)
    public PendingRoom findPendingRoomByHostUserId(String hostUserId) {
        if (hostUserId == null) {
            return null;
        }
        List<PendingRoom> hostRooms = pendingRoomRepository.findByHostUserId(hostUserId);
        if (hostRooms.isEmpty()) {
            return null;
        }
        PendingRoom hostRoom = hostRooms.get(0); 
        return hostRoom;
    }

    @Transactional(readOnly=true)
    public ActiveRoom findActiveRoomByGuestUserId(String guestUserId) {
        if (guestUserId == null) {
            return null;
        }
        List<ActiveRoom> guestRooms = activeRoomRepository.findByGuestUserId(guestUserId);
        if (guestRooms.isEmpty()) {
            return null;
        }
        ActiveRoom guestRoom = guestRooms.get(0); 
        return guestRoom;
    }

    @Transactional(readOnly=true)
    public ActiveRoom findActiveRoomById(String roomId) {
        if (roomId == null) {
            return null;
        }
        List<ActiveRoom> rooms = activeRoomRepository.findByRoomId(roomId);
        if (rooms.isEmpty()) {
            return null;
        }
        ActiveRoom room = rooms.get(0); 
        return room;
    }

    @Transactional(readOnly=true)
    public PendingRoom findPendingRoomById(String roomId) {
        if (roomId == null) {
            return null;
        }
        List<PendingRoom> rooms = pendingRoomRepository.findByRoomId(roomId);
        if (rooms.isEmpty()) {
            return null;
        }
        PendingRoom room = rooms.get(0); 
        return room;
    }

    @Transactional(readOnly=true)
    public boolean isUserInRoom(String userId) {
        return findActiveRoomByHostUserId(userId) != null || findActiveRoomByGuestUserId(userId) != null || findPendingRoomByHostUserId(userId) != null;
    }
}
