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
import com.example.yacht_backend.service.ApiService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;



@RestController
public class ApiController {
    private final ApiService apiService;

    ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return apiService.getAllRooms();
    }

    @PostMapping("/rooms/wait")
    public CreateNewRoomResponse createNewRoom(@RequestBody CreateNewRoomRequest createNewRoomRequest) {
        return apiService.createNewRoom(createNewRoomRequest.getUserId());
    }
    
    @PostMapping("/rooms/{roomId}/enter")
    public EnterRoomResponse enterRoom(@PathVariable String roomId, @RequestBody EnterRoomRequest enterRoomRequest) throws Exception {
        return apiService.enterRoom(roomId, enterRoomRequest.getUserId());
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
