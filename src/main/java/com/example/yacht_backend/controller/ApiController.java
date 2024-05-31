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
import com.example.yacht_backend.service.RoomService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class ApiController {
    private final RoomService roomService;

    ApiController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping("/rooms/wait")
    public CreateNewRoomResponse createNewRoom(@RequestBody CreateNewRoomRequest createNewRoomRequest) {
        return roomService.createNewRoom(createNewRoomRequest.getUserId());
    }
    
    @PostMapping("/rooms/{roomId}/enter")
    public EnterRoomResponse enterRoom(@PathVariable String roomId, @RequestBody EnterRoomRequest enterRoomRequest) throws Exception {
        return roomService.enterRoom(roomId, enterRoomRequest.getUserId());
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
