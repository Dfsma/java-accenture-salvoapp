package com.dfsma.salvo.controllers;


import com.dfsma.salvo.models.*;

import com.dfsma.salvo.repositories.GameRepository;
import com.dfsma.salvo.repositories.PlayerRepository;
import com.dfsma.salvo.service.GamePlayerService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerService gamePlayerService;

    @Autowired
    PlayerRepository playerRepository;

    @GetMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", !isGuest(authentication) ? makePlayersDTO(playerRepository.findByEmail(authentication.getName())) : "Guest");
        dto.put("games", gameRepository.findAll().stream().map(game -> this.makeGameDTO(game)).collect(Collectors.toList()));
        return dto;
    }

    public Map<String, Object> makeGameDTO(Game game){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> gamePlayer.getGamePlayerInfo()).collect(toList()));
        dto.put("scores", game.getScores().stream().map(score -> score.getScoresInfo()).collect(toList()));
        return dto;
    }


    @GetMapping("/game_view/{gamePlayer_id}")  //@RequestMapping(path = "/game_view/{gamePlayer_id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getGameView(@PathVariable Long gamePlayer_id){ //public ResponseEntity<Object> getGameView(@PathVariable Long gamePlayer_id){
        try{
            GamePlayer gamePlayer = gamePlayerService.findGamePlayerById(gamePlayer_id); //GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayer_id).orElse(null);
            return ResponseEntity.ok(this.makeGamePlayerDTO(gamePlayer));
        }catch (Exception exception){
            return  new ResponseEntity<>("Error: El GamePlayer Id: " + gamePlayer_id + " no existe.",HttpStatus.BAD_REQUEST);
        }
    }


    public Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        Game game = gamePlayer.getGame();
        Set<Ship> ship = gamePlayer.getShip();
        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getJoined());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayers -> gamePlayers.getGamePlayerInfo()).collect(toList()));
        dto.put("ships", ship.stream().map(ships -> ships.getShipsInfo(ships)));
        dto.put("salvoes", game.getGamePlayers().stream().flatMap(gp -> gp.getSalvos().stream().map(salvo -> salvo.getSalvosInfo())).collect(toList()));

        return dto;
    }


    public Map<String, Object> makePlayersDTO(Player player){

        Map<String, Object> dto = new HashMap<>();
        dto.put("id", player.getId());
        dto.put("userName", player.getUserName());
        dto.put("email", player.getEmail());

        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }




}
