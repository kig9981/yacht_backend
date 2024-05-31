package com.example.yacht_backend.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.dto.EnterRoomResponse;
import com.example.yacht_backend.domain.RoomData;


@Service
public class RoomService {
    private final RoomDatabaseService roomDatabaseService;
    private final UserDatabaseService userDatabaseService;
    private final ConcurrentHashMap<String, RoomData> roomGuestMap = new ConcurrentHashMap<>();

    RoomService(RoomDatabaseService roomDatabaseService, UserDatabaseService userDatabaseService) {
        this.roomDatabaseService = roomDatabaseService;
        this.userDatabaseService = userDatabaseService;
    }

    public List<Room> getAllRooms() {
        return roomDatabaseService.findAll();
    }
    
    public CreateNewRoomResponse createNewRoom(String sessionId, String hostData) {
        String userId = userDatabaseService.findUserIdBySessionId(sessionId);
        Room guestRoom = roomDatabaseService.findRoomByGuestUserId(userId);
        
        if (guestRoom != null) {
            DeferredResult<String> guestId = new DeferredResult<String>(60000L);
            guestId.setResult(null);
            DeferredResult<String> data = new DeferredResult<String>(60000L);
            data.setResult(null);
            return new CreateNewRoomResponse(null, guestId, data);
        }
        String roomId = UUID.randomUUID().toString(); // 임시로 임의의 룸을 생성
        DeferredResult<String> guestUserId = new DeferredResult<String>(60000L);
        DeferredResult<String> data = new DeferredResult<String>();
        
        RoomData roomData = new RoomData(roomId, userId, hostData, guestUserId, data);
        roomGuestMap.put(roomId, roomData);

        // timeout인 경우
        guestUserId.onTimeout(() -> {
            synchronized (roomData) {
                if(roomData.isValid()) {
                    roomData.setInvalid();
                    guestUserId.setResult(null);
                    data.setResult(null);
                }
                roomGuestMap.remove(roomId);
            }
        });

        // 어떤 이유에서든(네트워크 에러, 클라이언트쪽 timeout 등) 연결이 끊긴 경우 or 처리가 완료된 경우
        guestUserId.onCompletion(() -> {
            synchronized (roomData) {
                if (roomData.isValid()) {
                    roomData.setInvalid();
                    if (guestUserId.hasResult()) {
                        roomDatabaseService.save(new Room(roomId, userId, (String)guestUserId.getResult()));
                    }
                    else {
                        guestUserId.setResult(null);
                        data.setResult(null);
                    }
                }
                roomGuestMap.remove(roomId);
            }
        });
        return new CreateNewRoomResponse(roomId, guestUserId, data);
    }

    public EnterRoomResponse enterRoom(String roomId, String sessionId, String guestData) {
        String userId = userDatabaseService.findUserIdBySessionId(sessionId);
        if(roomDatabaseService.isUserInRoom(userId)) {
            return new EnterRoomResponse("Already in room", false);
        }
        RoomData roomData = roomGuestMap.get(roomId);

        if (roomData == null) {
            return new EnterRoomResponse("Room is full", false); // 일시적으로 끊어진 상태 or 이미 누군가 들어간 상태
        }

        synchronized (roomData) {
            if (roomData.isValid()) {
                roomData.setInvalid();
                String hostUserData = roomData.getHostUserData();
                DeferredResult<String> guestUserId = roomData.getGuestUserId();
                DeferredResult<String> guestUserData = roomData.getGuestUserData();
                if (guestUserId.hasResult()) {
                    return new EnterRoomResponse("Room is full", false);
                }
                guestUserId.setResult(userId);
                guestUserData.setResult(guestData);
                
                return new EnterRoomResponse(hostUserData, true);
            }
            return new EnterRoomResponse("Room is full", false);
        }
    }
}
