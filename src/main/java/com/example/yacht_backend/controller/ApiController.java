package com.example.yacht_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.yacht_backend.dto.CreateNewRoomRequest;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.dto.EnterRoomRequest;
import com.example.yacht_backend.dto.EnterRoomResponse;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.model.User;
import com.example.yacht_backend.service.RoomService;
import com.example.yacht_backend.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class ApiController {
    private final RoomService roomService;
    private final UserService userService;

    ApiController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createUser() {
        return userService.createUser();
    }

    @DeleteMapping("/leave-room")
    public void leaveRoom(@RequestBody String entity) {

    }

    @DeleteMapping("/kick-opponent")
    public void kickOpponent(@RequestBody String entity) {

    }

    @PostMapping("/start-game")
    public String startGame(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }

    @PostMapping("/roll-dice")
    public String rollDice(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @PostMapping("/finish-game")
    public String finishGame(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
}
