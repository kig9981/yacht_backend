package com.example.yacht_backend.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.websocket.WebSocketHandler;
import com.example.yacht_backend.dto.CreateNewRoomResponse;


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
        return new CreateNewRoomResponse(roomId, guestId);
    }

    public String enterRoom(String roomId, String userId) throws Exception {
        Room room = apiDatabaseService.findRoomById(roomId);
        if (room == null) {
            return "Room Not Exists";
        }
        if(apiDatabaseService.isUserInRoom(userId)) {
            return "Already in room";
        }
        boolean requestSuccess = WebSocketHandler.enterRoom(room.getHostUserId(), userId);
        if (requestSuccess) {
            return "PENDING";
        }
        return "REJECTED";
    }
}
