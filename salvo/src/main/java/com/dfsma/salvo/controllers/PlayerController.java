package com.dfsma.salvo.controllers;

import com.dfsma.salvo.dto.playerDTO;
import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class PlayerController {

    @Autowired
    PlayerService playerService;
    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/players")
    public List<Map<String, Object>> getPlayers() {
        return playerService.getPlayers().stream().map(player -> playerDTO.makePlayersAndScoresDTO(player)).collect(Collectors.toList());
    }

    @GetMapping(path = "/players/{player_id}")
    public ResponseEntity<Object> getPlayer(@PathVariable Long player_id){
        Player player = playerService.findPlayerById(player_id).orElse(null);
        return new ResponseEntity<>(playerDTO.makePlayersAndScoresDTO(player), HttpStatus.ACCEPTED);
    }

    @PostMapping("/players")
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password ){

        if(email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        if (playerService.findPlayerByEmail(email).isPresent()) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        playerService.savePlayer(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>("User Created", HttpStatus.CREATED);
    }





}
