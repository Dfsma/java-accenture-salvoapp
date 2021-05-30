package com.dfsma.salvo.controllers;

import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;


    @RequestMapping("/players")
    public List<Map<String, Object>> getPlayers() {
        return playerRepository.findAll().stream().map(player -> this.makePlayersDTO(player)).collect(Collectors.toList());
    }

    @RequestMapping(path = "/players/{player_id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getPlayer(@PathVariable Long player_id){
        Player player = playerRepository.findById(player_id).orElse(null);
        return new ResponseEntity<>(makePlayersDTO(player), HttpStatus.ACCEPTED);
    }

    public Map<String, Object> makePlayersDTO(Player player){
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", player.getId());
        dto.put("userName", player.getUserName());
        dto.put("email", player.getEmail());

        return dto;
    }
}
