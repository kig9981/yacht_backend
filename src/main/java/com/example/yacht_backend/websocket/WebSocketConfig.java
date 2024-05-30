package com.example.yacht_backend.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.yacht_backend.service.ApiDatabaseService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ApiDatabaseService apiDatabaseService;

    WebSocketConfig(ApiDatabaseService apiDatabaseService) {
        this.apiDatabaseService = apiDatabaseService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // TODO: setAllowedOrigins("*")를 수정
        registry.addHandler(new WebSocketHandler(apiDatabaseService), "/ws").setAllowedOrigins("*");
    }
}
