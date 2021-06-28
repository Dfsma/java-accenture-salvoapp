package com.dfsma.salvo.controllers;


import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.models.Salvo;

import com.dfsma.salvo.models.Score;
import com.dfsma.salvo.service.GamePlayerService;
import com.dfsma.salvo.service.PlayerService;
import com.dfsma.salvo.service.SalvoService;
import com.dfsma.salvo.service.ScoreService;
import com.dfsma.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    PlayerService playerService;

    @Autowired
    GamePlayerService gamePlayerService;

    @Autowired
    SalvoService salvoService;



    @PostMapping("/games/players/{gamePlayer_id}/salvoes")
    public ResponseEntity<Map<String, Object>> placeSalvos(@PathVariable Long gamePlayer_id, @RequestBody Salvo salvo, Authentication authentication){
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
            return new ResponseEntity<>(Util.makeMap("error", "This is not your game."), HttpStatus.UNAUTHORIZED);
        }

        int mySalvosSize = gamePlayer.getSalvoes().size();

        GamePlayer enemyGamePlayer = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> (gp != gamePlayer)).findAny().orElse(null);

        if(enemyGamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error", "You haven´t opponent."), HttpStatus.UNAUTHORIZED);
        }

        int enemySalvosSize = enemyGamePlayer.getSalvoes().size();

        if(salvo.getSalvoLocations().size() < 1 || salvo.getSalvoLocations().size() > 5){
            return new ResponseEntity<>(Util.makeMap("error", "Too Many shots in salvo."), HttpStatus.FORBIDDEN);
        }

        if(mySalvosSize > enemySalvosSize) {
            return new ResponseEntity<>(Util.makeMap("error", "Wait enemy shot."), HttpStatus.FORBIDDEN);
        }


        salvo.setTurn(mySalvosSize + 1 );
        salvo.setGamePlayer(gamePlayer);
        //gamePlayer.addSalvo(salvo);
        salvoService.saveSalvo(salvo);
        System.out.println("Turn: " + (mySalvosSize + 1));
        return new ResponseEntity<>(Util.makeMap("OK", "Your shots were created."), HttpStatus.CREATED);


    }
}
