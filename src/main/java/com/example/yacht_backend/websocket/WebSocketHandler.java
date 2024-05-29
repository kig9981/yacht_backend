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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends TextWebSocketHandler {
    private static Map<WebSocketSession, String> sessionUserMap  = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final RoomRepository roomRepository;

    WebSocketHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        if (query != null && query.startsWith("userId=")) {
            String userId = query.split("=")[1];
            List<Room> rooms = roomRepository.findByHostUserId(userId);
            if (rooms.size() == 1) {
                sessionUserMap.put(session, userId);
            } else {
                session.close(CloseStatus.SERVER_ERROR);
            }
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
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
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionUserMap.remove(session);
    }
}
