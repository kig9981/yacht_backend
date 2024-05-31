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
    private final ConcurrentHashMap<String, RoomData> roomGuestMap = new ConcurrentHashMap<>();

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
            DeferredResult<String> data = new DeferredResult<String>(60000L);
            data.setResult(null);
            return new CreateNewRoomResponse(null, guestId, data);
        }
        String roomId = UUID.randomUUID().toString(); // 임시로 임의의 룸을 생성
        DeferredResult<String> guestUserId = new DeferredResult<String>(60000L);
        DeferredResult<String> data = new DeferredResult<String>();
        
        RoomData roomData = new RoomData(roomId, userId, guestUserId, data);
        roomGuestMap.put(roomId, roomData); // TODO: guestId와 data를 가지는 클래스로 변경

        // timeout인 경우
        guestUserId.onTimeout(() -> {
            synchronized (roomData) {
                roomGuestMap.remove(roomId);
                guestUserId.setResult(null);
                data.setResult(null);
            }
        });

        // 어떤 이유에서든(네트워크 에러, 클라이언트쪽 timeout 등) 연결이 끊긴 경우 or 처리가 완료된 경우
        guestUserId.onCompletion(() -> {
            synchronized (roomData) {
                synchronized (guestUserId) {
                    if (guestUserId.hasResult()) {
                        roomDatabaseService.save(new Room(roomId, userId, (String)guestUserId.getResult()));
                    }
                    else {
                        guestUserId.setResult(null);
                        data.setResult(null);
                    }
                    roomGuestMap.remove(roomId);
                }
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
            String hostUserId = roomData.getHostUserId();
            DeferredResult<String> guestUserId = roomData.getGuestUserId();
            DeferredResult<String> data = roomData.getData();
            if (guestUserId.hasResult()) {
                return new EnterRoomResponse("Room is full", false);
            }
            guestUserId.setResult(userId);
            data.setResult(guestData);
            
            return new EnterRoomResponse(hostUserId, true);
        }
    }

    static class RoomData {
        private String roomId;
        private String hostUserId;
        private DeferredResult<String> guestUserId;
        private DeferredResult<String> data;

        public RoomData() {}

        public RoomData(String roomId, String hostUserId, DeferredResult<String> guestUserId, DeferredResult<String> data) {
            this.roomId = roomId;
            this.hostUserId = hostUserId;
            this.guestUserId = guestUserId;
            this.data = data;
        }

        public String getRoomId() {
            return this.roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getHostUserId() {
            return hostUserId;
        }

        public void setHostUserId(String hostUserId) {
            this.hostUserId = hostUserId;
        }

        public DeferredResult<String> getGuestUserId() {
            return guestUserId;
        }

        public void setGuestUserId(DeferredResult<String> guestUserId) {
            this.guestUserId = guestUserId;
        }

        public DeferredResult<String> getData() {
            return data;
        }

        public void setData(DeferredResult<String> data) {
            this.data = data;
        }
    }
}
