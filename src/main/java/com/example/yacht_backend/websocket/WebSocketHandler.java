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
    private static final ConcurrentHashMap<String, ScheduledFuture<?>> sessionTimeouts = new ConcurrentHashMap<>();
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
            List<Room> rooms = roomRepository.findByHostUserId(userId);
            if (rooms.size() == 1) {
                lock.lock();
                sessionUserMap.put(session.getId(), userId);
                lock.unlock();
            }
            else {
                session.close(CloseStatus.SERVER_ERROR);
            }
        }
        else if (query.startsWith("guestUserId=")) {
            String userId = query.split("=")[1];
            List<Room> rooms = roomRepository.findByGuestUserId(userId);
            if (rooms.size() == 1) {
                lock.lock();
                sessionUserMap.put(session.getId(), userId);
                ScheduledFuture<?> timeout = scheduler.schedule(() -> {
                    try {
                        if (session.isOpen()) {
                            session.close();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 1, TimeUnit.MINUTES);

                sessionTimeouts.put(session.getId(), timeout);
                lock.unlock();
            }
            else {
                session.close(CloseStatus.SERVER_ERROR);
            }
        }
        else {
            session.close(CloseStatus.BAD_DATA);
        }
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
        sessionUserMap.remove(sessionId);
        ScheduledFuture<?> timeout = sessionTimeouts.remove(sessionId);
        if (timeout != null) {
            timeout.cancel(true);
        }
        lock.unlock();
    }
}
