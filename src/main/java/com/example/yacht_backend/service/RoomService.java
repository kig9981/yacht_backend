package com.example.yacht_backend.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import com.example.yacht_backend.model.ActiveRoom;
import com.example.yacht_backend.model.PendingRoom;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.dto.EnterRoomResponse;
import com.example.yacht_backend.domain.RoomData;


@Service
public class RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    private final RoomDatabaseService roomDatabaseService;
    private final UserDatabaseService userDatabaseService;
    private final ConcurrentHashMap<String, RoomData> roomGuestMap;
    private Long deferredResultTimeout = 60000L;
    private boolean onCompletion = true;

    RoomService(RoomDatabaseService roomDatabaseService, UserDatabaseService userDatabaseService, ConcurrentHashMap<String, RoomData> roomGuestMap) {
        this.roomDatabaseService = roomDatabaseService;
        this.userDatabaseService = userDatabaseService;
        this.roomGuestMap = roomGuestMap;
    }

    public void setDefferedResultTimeout(Long deferredResultTimeout) {
        this.deferredResultTimeout = deferredResultTimeout;
    }

    public void enableOnCompletion(boolean onCompletion) {
        this.onCompletion = onCompletion;
    }

    public List<PendingRoom> getAllRooms() {
        logger.info("RoomService.getAllRooms 실행");
        return roomDatabaseService.findAllPendingRoom();
    }
    
    public DeferredResult<CreateNewRoomResponse> createNewRoom(String sessionId, String hostData) {
        logger.info("RoomService.createNewRoom 실행");
        String userId = userDatabaseService.findUserIdBySessionId(sessionId);
        ActiveRoom guestRoom = roomDatabaseService.findActiveRoomByGuestUserId(userId);
        
        if (guestRoom != null) {
            DeferredResult<CreateNewRoomResponse> createNewRoomResponse = new DeferredResult<>();
            createNewRoomResponse.setResult(new CreateNewRoomResponse(null, null, null));
            return createNewRoomResponse;
        }
        String roomId = UUID.randomUUID().toString(); // 임시로 임의의 룸을 생성
        DeferredResult<CreateNewRoomResponse> createNewRoomResponse = new DeferredResult<>(deferredResultTimeout);
        
        RoomData roomData = new RoomData(roomId, userId, hostData, createNewRoomResponse);
        roomGuestMap.put(roomId, roomData);
        PendingRoom pendingRoom = new PendingRoom(roomId, userId, hostData);
        roomDatabaseService.save(pendingRoom);

        // timeout인 경우
        createNewRoomResponse.onTimeout(() -> {
            synchronized (roomData) {
                if(roomData.isOpen()) {
                    roomData.close();
                    createNewRoomResponse.setResult(new CreateNewRoomResponse(null, null, null));
                }
                roomGuestMap.remove(roomId);
                roomDatabaseService.delete(pendingRoom);
            }
        });

        // 어떤 이유에서든(네트워크 에러, 클라이언트쪽 timeout 등) 연결이 끊긴 경우 or 처리가 완료된 경우
        createNewRoomResponse.onCompletion(() -> {
            if (onCompletion) {
                synchronized (roomData) {
                    if (roomData.isOpen()) {
                        roomData.close();
                        if (createNewRoomResponse.hasResult()) {
                            CreateNewRoomResponse response = (CreateNewRoomResponse)createNewRoomResponse.getResult();
                            roomDatabaseService.save(new ActiveRoom(roomId, userId, response.getGuestUserId()));
                        }
                        else {
                            createNewRoomResponse.setResult(new CreateNewRoomResponse(null, null, null));
                        }
                    }
                    roomGuestMap.remove(roomId);
                    roomDatabaseService.delete(pendingRoom);
                }
            }
        });
        return createNewRoomResponse;
    }

    public EnterRoomResponse enterRoom(String roomId, String sessionId, String guestData) {
        logger.info("RoomService.enterRoom 실행");
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
                DeferredResult<CreateNewRoomResponse> createNewRoomResponse = roomData.getCreateNewRoomResponse();
                if (createNewRoomResponse.hasResult()) {
                    return new EnterRoomResponse("Room is full", false);
                }
                createNewRoomResponse.setResult(new CreateNewRoomResponse(roomId, userId, guestData));
                
                return new EnterRoomResponse(hostUserId, true);
            }
            return new EnterRoomResponse("Room is full", false);
        }
    }
}
