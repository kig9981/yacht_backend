package com.example.yacht_backend.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import com.example.yacht_backend.model.ActiveRoom;
import com.example.yacht_backend.model.PendingRoom;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.dto.EnterRoomResponse;
import com.example.yacht_backend.domain.RoomData;


@Service
public class RoomService {
    private final RoomDatabaseService roomDatabaseService;
    private final UserDatabaseService userDatabaseService;
    private final ConcurrentHashMap<String, RoomData> roomGuestMap;
    private Long deferredResultTimeout = 60000L;

    RoomService(RoomDatabaseService roomDatabaseService, UserDatabaseService userDatabaseService, ConcurrentHashMap<String, RoomData> roomGuestMap) {
        this.roomDatabaseService = roomDatabaseService;
        this.userDatabaseService = userDatabaseService;
        this.roomGuestMap = roomGuestMap;
    }

    public void setDefferedResultTimeout(Long deferredResultTimeout) {
        this.deferredResultTimeout = deferredResultTimeout;
    }

    public List<PendingRoom> getAllRooms() {
        return roomDatabaseService.findAllPendingRoom();
    }
    
    public CreateNewRoomResponse createNewRoom(String sessionId, String hostData) {
        String userId = userDatabaseService.findUserIdBySessionId(sessionId);
        ActiveRoom guestRoom = roomDatabaseService.findRoomByGuestUserId(userId);
        
        if (guestRoom != null) {
            DeferredResult<String> guestId = new DeferredResult<String>(deferredResultTimeout);
            guestId.setResult(null);
            DeferredResult<String> data = new DeferredResult<String>(deferredResultTimeout);
            data.setResult(null);
            return new CreateNewRoomResponse(null, guestId, data);
        }
        String roomId = UUID.randomUUID().toString(); // 임시로 임의의 룸을 생성
        DeferredResult<String> guestUserId = new DeferredResult<String>(deferredResultTimeout);
        DeferredResult<String> data = new DeferredResult<String>();
        
        RoomData roomData = new RoomData(roomId, userId, hostData, guestUserId, data);
        roomGuestMap.put(roomId, roomData);
        PendingRoom pendingRoom = new PendingRoom(roomId, userId, hostData);
        roomDatabaseService.save(pendingRoom);

        // timeout인 경우
        guestUserId.onTimeout(() -> {
            synchronized (roomData) {
                if(roomData.isOpen()) {
                    roomData.close();
                    guestUserId.setResult(null);
                    data.setResult(null);
                }
                roomGuestMap.remove(roomId);
                roomDatabaseService.delete(pendingRoom);
            }
        });

        // 어떤 이유에서든(네트워크 에러, 클라이언트쪽 timeout 등) 연결이 끊긴 경우 or 처리가 완료된 경우
        guestUserId.onCompletion(() -> {
            synchronized (roomData) {
                if (roomData.isOpen()) {
                    roomData.close();
                    if (guestUserId.hasResult()) {
                        roomDatabaseService.save(new ActiveRoom(roomId, userId, (String)guestUserId.getResult()));
                    }
                    else {
                        guestUserId.setResult(null);
                        data.setResult(null);
                    }
                }
                roomGuestMap.remove(roomId);
                roomDatabaseService.delete(pendingRoom);
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
            if (roomData.isOpen()) {
                roomData.close();
                String hostUserId = roomData.getHostUserId();
                DeferredResult<String> guestUserId = roomData.getGuestUserId();
                DeferredResult<String> guestUserData = roomData.getGuestUserData();
                if (guestUserId.hasResult()) {
                    return new EnterRoomResponse("Room is full", false);
                }
                guestUserId.setResult(userId);
                guestUserData.setResult(guestData);
                
                return new EnterRoomResponse(hostUserId, true);
            }
            return new EnterRoomResponse("Room is full", false);
        }
    }
}
