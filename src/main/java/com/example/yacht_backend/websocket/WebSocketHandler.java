package com.example.yacht_backend.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.yacht_backend.dto.WebSocketMessage;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.repository.RoomRepository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class WebSocketHandler extends TextWebSocketHandler {
    private static final ConcurrentHashMap<String, String> sessionUserMap  = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, WebSocketSession> userSessionMap  = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ScheduledFuture<?>> sessionTimeouts = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> pendingRequests  = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final ReentrantLock lock = new ReentrantLock();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final RoomRepository roomRepository;

    WebSocketHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String query = session.getUri().getQuery();
        if (query == null) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        String[] queryPairs = query.split("&");
        if (queryPairs.length != 1) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        if (query.startsWith("hostUserId=")) {
            String userId = query.split("=")[1];
            handleHostConnection(session, userId);
        }
        else if (query.startsWith("guestUserId=")) {
            String userId = query.split("=")[1];
            handleGuestConnection(session, userId);
        }
        else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    void handleHostConnection(WebSocketSession session, String hostId) throws Exception {
        List<Room> rooms = roomRepository.findByHostUserId(hostId);
        if (rooms.size() != 1) {
            session.close(CloseStatus.SERVER_ERROR);
            return;
        }
        lock.lock();
        sessionUserMap.put(session.getId(), hostId);
        userSessionMap.put(hostId, session);
        lock.unlock();
    }

    void handleGuestConnection(WebSocketSession session, String guestId) throws Exception {
        List<Room> rooms = roomRepository.findByGuestUserId(guestId);
        if (rooms.size() != 1) {
            session.close(CloseStatus.SERVER_ERROR);
            return;
        }
        lock.lock();
        sessionUserMap.put(session.getId(), guestId);
        userSessionMap.put(guestId, session);

        String hostId = pendingRequests.get(guestId);
        
        if (hostId == null) {
            ScheduledFuture<?> timeout = scheduler.schedule(() -> {
                try {
                    if (session.isOpen()) {
                        // TODO: timeout 보내기
                        session.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1, TimeUnit.MINUTES);
            sessionTimeouts.put(session.getId(), timeout);
            
        }
        else if (hostId == guestId) {
            // host로부터 이미 거절요청을 받음.
            // TODO: reject 보내기
            session.close();
        }
        else {
            // host로부터 이미 수락요청을 받음.
            WebSocketSession hostSession = userSessionMap.get(hostId);
            // TODO: accept 보내기
            session.close();
        }
        lock.unlock();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String payload = message.getPayload();
        WebSocketMessage data = objectMapper.readValue(payload, WebSocketMessage.class);

        String roomId = data.getRoomId();
        String hostUserId = data.getHostUserId();
        String guestUserId = data.getGuestUserId();
        boolean acceptEnter = data.getAcceptEnter();

        if(acceptEnter) {
            //TODO: implement
        }
        else {
            //TODO: implement
            // WebSocketSession guestUserSession = sessionUserMap.get
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        String sessionId = session.getId();
        lock.lock();
        String userId = sessionUserMap.get(sessionId);
        sessionUserMap.remove(sessionId);
        userSessionMap.remove(userId);
        ScheduledFuture<?> timeout = sessionTimeouts.remove(sessionId);
        if (timeout != null) {
            timeout.cancel(true);
        }
        lock.unlock();
    }
}
