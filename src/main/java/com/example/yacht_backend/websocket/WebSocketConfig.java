package com.example.yacht_backend.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.yacht_backend.repository.RoomRepository;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final RoomRepository roomRepository;

    WebSocketConfig(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // TODO: setAllowedOrigins("*")를 수정
        registry.addHandler(new WebSocketHandler(roomRepository), "/ws").setAllowedOrigins("*");
    }
}
