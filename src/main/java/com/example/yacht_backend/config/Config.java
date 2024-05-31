package com.example.yacht_backend.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.yacht_backend.domain.RoomData;

@Configuration
public class Config {
    @Bean
    public ConcurrentHashMap<String, RoomData> roomGuestMap() {
        return new ConcurrentHashMap<String, RoomData>();
    }
}
