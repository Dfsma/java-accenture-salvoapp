package com.dfsma.salvo.controllers;


import com.dfsma.salvo.dto.gameDTO;
import com.dfsma.salvo.dto.gamePlayerDTO;
import com.dfsma.salvo.dto.playerDTO;
import com.dfsma.salvo.models.*;

import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.repositories.GameRepository;
import com.dfsma.salvo.repositories.PlayerRepository;
import com.dfsma.salvo.repositories.ScoreRepository;
import com.dfsma.salvo.service.GamePlayerService;
import com.dfsma.salvo.service.PlayerService;
import com.dfsma.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    GamePlayerService gamePlayerService;

    @Autowired
    PlayerService playerService;


    @PostMapping("/games")
    public ResponseEntity<Object> createGame(Authentication authentication){
        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByEmail(authentication.getName()).orElse(null);
        if(player == null) {
            return new ResponseEntity<>(Util.makeMap("error", "Player not found."), HttpStatus.NOT_FOUND);
        }

        Game game = new Game();
        gameRepository.save(game);

        GamePlayer gamePlayer = new GamePlayer(player, game, LocalDateTime.now());
        gamePlayerRepository.save(gamePlayer);

        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/game_view/{gamePlayer_id}")
    public ResponseEntity<Object> getGameView(@PathVariable Long gamePlayer_id, Authentication authentication){
        try{
            if(Util.isGuest(authentication)){
                return new ResponseEntity<>(Util.makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
            }

            Player player = playerService.findPlayerByEmail(authentication.getName());

            if(player == null) {
                return new ResponseEntity<>(Util.makeMap("error", "Player not found."), HttpStatus.NOT_FOUND);
            }

            GamePlayer gamePlayer = gamePlayerService.findGamePlayerById(gamePlayer_id);
            System.out.println(gamePlayer.getPlayer().getId());

            if(gamePlayer.getPlayer().getId() == player.getId()){
                    return new ResponseEntity<>(gamePlayerDTO.makeGamePlayerDTO(gamePlayer), HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>(Util.makeMap("error", "You´re not in this game"), HttpStatus.NOT_FOUND);
            }

        }catch (Exception exception){
            return  new ResponseEntity<>("Error: El GamePlayer Id: " + gamePlayer_id + " no existe.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/game/{game_id}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long game_id, Authentication authentication){

        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByEmail(authentication.getName()).orElse(null);
        if(player == null) {
            return new ResponseEntity<>(Util.makeMap("error", "Player not found."), HttpStatus.NOT_FOUND);
        }

        Game game = gameRepository.findById(game_id).orElse(null);
        if(game == null){
            return new ResponseEntity<>(Util.makeMap("error", "Game" + game_id + "Not Found"), HttpStatus.NOT_FOUND);
        }

        if(game.getPlayers().contains(player)){
            return new ResponseEntity<>(Util.makeMap("error", "You're already in the game"), HttpStatus.FORBIDDEN);
        }

        if(game.getGamePlayers().size() >= 2){
            return new ResponseEntity<>(Util.makeMap("error", "The game is full"), HttpStatus.FORBIDDEN);
        }

        GamePlayer gamePlayer = new GamePlayer(player, game, LocalDateTime.now());
        if(gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error", "Couldn't create GamePlayer"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        gamePlayerRepository.save(gamePlayer);
        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);

    }

    @GetMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", Util.isGuest(authentication) ? "Guest" : playerDTO.makePlayersDTO(playerRepository.findByEmail(authentication.getName()).orElse(null)));
        dto.put("games", gameRepository.findAll().stream().map(game -> gameDTO.makeGameDTO(game)).collect(Collectors.toList()));
        return dto;
    }





}
