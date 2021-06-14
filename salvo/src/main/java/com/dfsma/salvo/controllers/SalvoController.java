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

import java.util.List;
import java.util.Map;
import java.util.Set;
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
            return new ResponseEntity<>(Util.makeMap("error", "This is not your game!"), HttpStatus.UNAUTHORIZED);
        }

        int mySalvos = gamePlayer.getSalvos().size();
        int enemySalvos = gamePlayer.getGame().getGamePlayers().stream().filter(gamePlayer1 -> gamePlayer1.getPlayer() != gamePlayer.getPlayer()).collect(Collectors.toList()).size();

        if (mySalvos > enemySalvos){
            return new ResponseEntity<>(Util.makeMap("error", "Wait for enemy turn"), HttpStatus.FORBIDDEN);
        }

        if ( salvo.getSalvoLocations().size() <=1 && salvo.getSalvoLocations().size() >=5){
            return new ResponseEntity<>(Util.makeMap("error", "A player try to do more than 5 shots"), HttpStatus.FORBIDDEN);

        }

        int myTurn = gamePlayer.getSalvos().size();
        salvo.setTurn(myTurn + 1 );
        gamePlayer.addSalvo(salvo);
        salvoRepository.save(salvo);
        return new ResponseEntity<>(Util.makeMap("OK", "Your salvoes were fired!"), HttpStatus.CREATED);


        /*
        if(salvo.getLocations().size() > 5){
            return new ResponseEntity<>(Util.makeMap("error", "A player try to do more than 5 shots"), HttpStatus.FORBIDDEN);
        }
        else {
            //Long turnRepeat = mySalvos.stream().filter(salvo1 -> salvo1.getTurn() == salvo.getTurn()).collect(Collectors.counting());
            int turnRepeat = mySalvos.size();
            System.out.println("turnRepeat: " + turnRepeat);
            if(turnRepeat > 0) {
                return new ResponseEntity<>(Util.makeMap("error", "player firing a salvo more than once per turn"), HttpStatus.FORBIDDEN);
            }else {
                salvo.setTurn(turnRepeat+1);
                gamePlayer.addSalvo(salvo);
                salvoRepository.save(salvo);
                return new ResponseEntity<>(Util.makeMap("OK", "Your salvoes were fired!"), HttpStatus.CREATED);
            }
        }
         */
    }
}
