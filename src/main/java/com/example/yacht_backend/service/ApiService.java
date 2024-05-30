package com.example.yacht_backend.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.dto.EnterRoomResponse;
import com.example.yacht_backend.exception.RoomNotFoundException;


@Service
public class ApiService {
    private final ApiDatabaseService apiDatabaseService;
    private final ConcurrentHashMap<String, DeferredResult<String>> roomGuestMap = new ConcurrentHashMap<>();

    ApiService(ApiDatabaseService apiDatabaseService) {
        this.apiDatabaseService = apiDatabaseService;
    }

    public List<Room> getAllRooms() {
        return apiDatabaseService.findAll();
    }
    
    public CreateNewRoomResponse createNewRoom(String userId) {
        Room hostRoom = apiDatabaseService.findRoomByHostUserId(userId);
        Room guestRoom = apiDatabaseService.findRoomByGuestUserId(userId);
        if (guestRoom != null) {
            DeferredResult<String> guestId = new DeferredResult<String>(60000L);
            guestId.setResult(null);
            return new CreateNewRoomResponse(null, guestId);
        }
        String tempRoomId = null;
        if (hostRoom != null) {
            tempRoomId = hostRoom.getRoomId();
        }
        else {
            tempRoomId = UUID.randomUUID().toString();
            Room newRoom = new Room(tempRoomId, userId, null);
            apiDatabaseService.save(newRoom);
        }
        String roomId = tempRoomId;
        DeferredResult<String> guestId = new DeferredResult<String>(60000L);
        
        roomGuestMap.put(roomId, guestId);
        guestId.onTimeout(() -> {
            synchronized (guestId) {
                roomGuestMap.remove(roomId);
                guestId.setResult(null);
            }
        });

        guestId.onCompletion(() -> {
            synchronized (guestId) {
                if (!guestId.hasResult()) {
                    roomGuestMap.remove(roomId);
                    guestId.setResult(null);
                }
            }
        });
        return new CreateNewRoomResponse(roomId, guestId);
    }

    public EnterRoomResponse enterRoom(String roomId, String userId) {
        Room room = apiDatabaseService.findRoomById(roomId);
        if (room == null) {
            return new EnterRoomResponse("Room Not Exists", false);
        }
        if(apiDatabaseService.isUserInRoom(userId)) {
            return new EnterRoomResponse("Already in room", false);
        }
        DeferredResult<String> guestResult = roomGuestMap.get(roomId);

        if (guestResult == null) {
            return new EnterRoomResponse("Room is full", false); // 일시적으로 끊어진 상태 or 이미 누군가 들어간 상태
        }

        synchronized (guestResult) {
            if (guestResult.hasResult()) {
                return new EnterRoomResponse("Room is full", false);
            }
            String hostUserId = room.getHostUserId();
            try {
                apiDatabaseService.addGuestUserToRoom(hostUserId, userId);
            }
            catch (RoomNotFoundException e) {
                return new EnterRoomResponse("unknown error", false);
            }
            guestResult.setResult(userId);
            roomGuestMap.remove(roomId);
            
            return new EnterRoomResponse(hostUserId, true);
        }
        
    }
}
