package com.dfsma.salvo.controllers;

import com.dfsma.salvo.dto.playerDTO;
import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.repositories.PlayerRepository;
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
    PlayerRepository playerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/players")
    public List<Map<String, Object>> getPlayers() {
        return playerRepository.findAll().stream().map(player -> playerDTO.makePlayersAndScoresDTO(player)).collect(Collectors.toList());
    }

    @GetMapping(path = "/players/{player_id}")
    public ResponseEntity<Object> getPlayer(@PathVariable Long player_id){
        Player player = playerRepository.findById(player_id).orElse(null);
        return new ResponseEntity<>(playerDTO.makePlayersAndScoresDTO(player), HttpStatus.ACCEPTED);
    }

    @PostMapping("/players")
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password ){

        if(email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email).isPresent()) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>("User Created", HttpStatus.CREATED);
    }





}
