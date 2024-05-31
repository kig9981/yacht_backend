package com.example.yacht_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yacht_backend.dto.CreateNewRoomRequest;
import com.example.yacht_backend.dto.CreateNewRoomResponse;
import com.example.yacht_backend.dto.EnterRoomRequest;
import com.example.yacht_backend.dto.EnterRoomResponse;
import com.example.yacht_backend.model.Room;
import com.example.yacht_backend.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping("/wait")
    public CreateNewRoomResponse createNewRoom(@RequestBody CreateNewRoomRequest createNewRoomRequest) {
        return roomService.createNewRoom(createNewRoomRequest.getSessionId(), createNewRoomRequest.getData());
    }
    
    @PostMapping("/{roomId}/enter")
    public EnterRoomResponse enterRoom(@PathVariable String roomId, @RequestBody EnterRoomRequest enterRoomRequest) throws Exception {
        return roomService.enterRoom(roomId, enterRoomRequest.getSessionId(), enterRoomRequest.getData());
    }
}
