package com.dfsma.salvo.controllers;


import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.models.Ship;
import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.repositories.PlayerRepository;
import com.dfsma.salvo.repositories.ShipRepository;
import com.dfsma.salvo.service.GamePlayerService;
import com.dfsma.salvo.service.PlayerService;
import com.dfsma.salvo.service.ShipService;
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
    ShipService shipService;

    @Autowired
    GamePlayerService gamePlayerService;

    @Autowired
    PlayerService playerService;

    @PostMapping("/games/players/{gamePlayer_id}/ships")
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable Long gamePlayer_id, @RequestBody List<Ship> ships, Authentication authentication){
        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerService.findPlayerByEmail(authentication.getName()).orElse(null);

        if(player == null) {
            return new ResponseEntity<>(Util.makeMap("error", "Player not found."), HttpStatus.NOT_FOUND);
        }

        GamePlayer gamePlayer = gamePlayerService.findGamePlayerById(gamePlayer_id).orElse(null);
        if(gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error", "Game player not found."), HttpStatus.FORBIDDEN);
        }
        if(gamePlayer.getPlayer() != player){
            return new ResponseEntity<>(Util.makeMap("error", "This is not your game!"), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer.getShips().size() == 5){
            return new ResponseEntity<>(Util.makeMap("error", "Ships already placed."), HttpStatus.FORBIDDEN);
        }

        for(Ship ship : ships){
            if( ship.getType().equals("carrier") && ship.getShipLocations().size() !=  5 ){
                return new ResponseEntity<>(Util.makeMap("error", "length of "+ ship.getType()  +" not correct"), HttpStatus.FORBIDDEN);
            }
            if (ship.getType().equals("battleship") && ship.getShipLocations().size() != 4){
                return new ResponseEntity<>(Util.makeMap("error", "length of "+ ship.getType()  +" not correct"), HttpStatus.FORBIDDEN);
            }
            if (ship.getType().equals("submarine") && ship.getShipLocations().size() != 3){
                return new ResponseEntity<>(Util.makeMap("error", "length of "+ ship.getType()  +" not correct"), HttpStatus.FORBIDDEN);
            }
            if (ship.getType().equals("destroyer") && ship.getShipLocations().size() != 3){
                return new ResponseEntity<>(Util.makeMap("error", "length of "+ ship.getType()  +" not correct"), HttpStatus.FORBIDDEN);
            }
            if (ship.getType().equals("patrolboat") && ship.getShipLocations().size() != 2){
                return new ResponseEntity<>(Util.makeMap("error", "length of "+ ship.getType()  +" not correct"), HttpStatus.FORBIDDEN);
            }
        }

        ships.forEach(ship -> ship.setGamePlayer(gamePlayer));
        shipService.saveAllShips(ships);
        /*for(Ship ship : ships){gamePlayer.addShip(ship);}*/

        return new ResponseEntity(Util.makeMap("OK", "Ships Placed Correctly"), HttpStatus.CREATED);

    }


}
