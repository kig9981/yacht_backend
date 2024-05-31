package com.example.yacht_backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class ApiController {

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
