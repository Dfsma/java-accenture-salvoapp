package com.dfsma.salvo.controllers;


import com.dfsma.salvo.models.Game;
import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.models.Ship;
import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summarizingDouble;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;


    @RequestMapping("/games")
    public List<Map<String, Object>> getGames() {
        return gameRepository.findAll().stream().map(game -> this.makeGameDTO(game)).collect(Collectors.toList());
    }


    @RequestMapping(path = "/game_view/{gamePlayer_id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getGameView(@PathVariable Long gamePlayer_id){
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayer_id).orElse(null);
        return new ResponseEntity<>(makeGamePlayerDTO(gamePlayer), HttpStatus.ACCEPTED);
    }




    public Map<String, Object> makeGameDTO(Game game){
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> gamePlayer.getGamePlayerInfo()).collect(toList()));
        return dto;
    }
    public Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new HashMap<>();
        Game game = gamePlayer.getGame();
        Set<Ship> ship = gamePlayer.getShip();

        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getJoined());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayers -> gamePlayers.getGamePlayerInfo()).collect(toList()));
        dto.put("ships", ship.stream().map(ships -> ships.getShipsInfo(ships)));
        return dto;
    }








}
