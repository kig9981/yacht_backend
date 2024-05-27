package com.example.yacht_backend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class ApiController {
    

    @GetMapping("/get-all-rooms")
    public String getAllRooms(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/create-new-room")
    public String createNewRoom(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @PostMapping("/enter-room")
    public String enterRoom(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
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
