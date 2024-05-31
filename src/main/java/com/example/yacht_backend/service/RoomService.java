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
public class RoomService {
    private final RoomDatabaseService roomDatabaseService;
    private final UserDatabaseService userDatabaseService;
    private final ConcurrentHashMap<String, DeferredResult<String>> roomGuestMap = new ConcurrentHashMap<>();

    RoomService(RoomDatabaseService roomDatabaseService, UserDatabaseService userDatabaseService) {
        this.roomDatabaseService = roomDatabaseService;
        this.userDatabaseService = userDatabaseService;
    }

    public List<Room> getAllRooms() {
        return roomDatabaseService.findAll();
    }
    
    public CreateNewRoomResponse createNewRoom(String sessionId) {
        String userId = userDatabaseService.findUserIdBySessionId(sessionId);
        Room guestRoom = roomDatabaseService.findRoomByGuestUserId(userId);
        
        if (guestRoom != null) {
            DeferredResult<String> guestId = new DeferredResult<String>(60000L);
            guestId.setResult(null);
            return new CreateNewRoomResponse(null, guestId);
        }
        String roomId = UUID.randomUUID().toString(); // 임시로 임의의 룸을 생성
        DeferredResult<String> guestUserId = new DeferredResult<String>(60000L);
        
        roomGuestMap.put(roomId, guestUserId);
        guestUserId.onTimeout(() -> {
            synchronized (guestUserId) {
                roomGuestMap.remove(roomId);
                guestUserId.setResult(null);
            }
        });

        guestUserId.onCompletion(() -> {
            synchronized (guestUserId) {
                if (!guestUserId.hasResult()) {
                    roomGuestMap.remove(roomId);
                    guestUserId.setResult(null);
                }
            }
        });
        return new CreateNewRoomResponse(roomId, guestUserId);
    }

    public EnterRoomResponse enterRoom(String roomId, String userId) {
        Room room = roomDatabaseService.findRoomById(roomId);
        if (room == null) {
            return new EnterRoomResponse("Room Not Exists", false);
        }
        if(roomDatabaseService.isUserInRoom(userId)) {
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
                roomDatabaseService.addGuestUserToRoom(hostUserId, userId);
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
