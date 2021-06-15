package com.dfsma.salvo.controllers;


import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.models.Salvo;
import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.repositories.PlayerRepository;
import com.dfsma.salvo.repositories.SalvoRepository;
import com.dfsma.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    SalvoRepository salvoRepository;

    @PostMapping("/games/players/{gamePlayer_id}/salvos")
    public ResponseEntity<Map<String, Object>> placeSalvos(@PathVariable Long gamePlayer_id, @RequestBody Salvo salvo, Authentication authentication){
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
            return new ResponseEntity<>(Util.makeMap("error", "This is not your game."), HttpStatus.UNAUTHORIZED);
        }

        int mySalvosSize = gamePlayer.getSalvoes().size();

        GamePlayer enemyGamePlayer = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> (gp != gamePlayer)).findAny().orElse(null);
        //System.out.println("enemy email " + enemyGamePlayer.getPlayer().getEmail());

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
        salvoRepository.save(salvo);
        System.out.println("Turn: " + (mySalvosSize + 1));
        return new ResponseEntity<>(Util.makeMap("OK", "Your shots were created."), HttpStatus.CREATED);


    }
}
