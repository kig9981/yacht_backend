package com.example.yacht_backend.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.yacht_backend.dto.WebSocketMessage;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.repository.RoomRepository;

import java.util.List;

public class WebSocketHandler extends TextWebSocketHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private final RoomRepository roomRepository;

    WebSocketHandler(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        WebSocketMessage data = objectMapper.readValue(payload, WebSocketMessage.class);

        String roomId = data.getRoomId();
        List<Room> rooms = roomRepository.findByRoomId(roomId);

        if(rooms.size()>1) {
            throw new Exception("Room Id not unique");
        }

        Room room = rooms.get(0);

        if(data.getAcceptEnter()) {
            //TODO: implement
        }
        else {
            //TODO: implement
        }
    }

}
