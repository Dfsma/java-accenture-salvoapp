package com.dfsma.salvo.controllers;


import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.models.Ship;
import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.repositories.PlayerRepository;
import com.dfsma.salvo.repositories.ShipRepository;
import com.dfsma.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    ShipRepository shipRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @PostMapping("/games/players/{gamePlayer_id}/ships")
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable Long gamePlayer_id, @RequestBody List<Ship> ships, Authentication authentication){
        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByEmail(authentication.getName()).orElse(null);

        if(player == null) {
            return new ResponseEntity<>(Util.makeMap("error", "Player not found."), HttpStatus.NOT_FOUND);
        }

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayer_id).orElse(null);
        if(gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error", "Game player not found."), HttpStatus.FORBIDDEN);
        }
        if(gamePlayer.getPlayer() != player){
            return new ResponseEntity<>(Util.makeMap("error", "This is not your game!"), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer.getShips().size() == 5){
            return new ResponseEntity<>(Util.makeMap("error", "Ships already placed."), HttpStatus.FORBIDDEN);
        }

        ships.forEach(ship -> ship.setGamePlayer(gamePlayer));
        shipRepository.saveAll(ships);
        /*for(Ship ship : ships){gamePlayer.addShip(ship);}*/

        return new ResponseEntity(Util.makeMap("created", "true"), HttpStatus.CREATED);

    }

}
