package com.dfsma.salvo.controllers;


import com.dfsma.salvo.dto.gameDTO;
import com.dfsma.salvo.dto.gamePlayerDTO;
import com.dfsma.salvo.dto.playerDTO;
import com.dfsma.salvo.models.*;

import com.dfsma.salvo.service.GamePlayerService;
import com.dfsma.salvo.service.GameService;
import com.dfsma.salvo.service.PlayerService;
import com.dfsma.salvo.service.ScoreService;
import com.dfsma.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    GamePlayerService gamePlayerService;

    @Autowired
    PlayerService playerService;

    @Autowired
    ScoreService scoreService;

    @GetMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", Util.isGuest(authentication) ? "Guest" : playerDTO.makePlayersDTO(playerService.findPlayerByEmail(authentication.getName()).orElse(null)));
        dto.put("games", gameService.getGames().stream().map(game -> gameDTO.makeGameDTO(game)).collect(Collectors.toList()));
        return dto;
    }

    @PostMapping("/games")
    public ResponseEntity<Object> createGame(Authentication authentication){

        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerService.findPlayerByEmail(authentication.getName()).orElse(null);

        if(player == null) {
            return new ResponseEntity<>(Util.makeMap("error", "Player not found."), HttpStatus.NOT_FOUND);
        }

        Game game = new Game();
        gameService.saveGame(game);

        GamePlayer gamePlayer = new GamePlayer(player, game, LocalDateTime.now());
        gamePlayerService.saveGamePlayer(gamePlayer);

        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/game_view/{gamePlayer_id}")
    public ResponseEntity<Object> getGameView(@PathVariable Long gamePlayer_id, Authentication authentication){
        try{
            if(Util.isGuest(authentication)){
                return new ResponseEntity<>(Util.makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
            }

            Player player = playerService.findPlayerByEmail(authentication.getName()).orElse(null);

            if(player == null) {
                return new ResponseEntity<>(Util.makeMap("error", "Player not found."), HttpStatus.NOT_FOUND);
            }

            GamePlayer gamePlayer = gamePlayerService.findGamePlayerById(gamePlayer_id).orElse(null);
            System.out.println(gamePlayer.getPlayer().getId());

            /*
            Util.addScoreToFinishedGame(gamePlayer);
            */

            if(Util.setGameState(gamePlayer) == "WON"){
                if(gamePlayer.getGame().getScores().size()<2) {
                    Score score1 = new Score();
                    Set<Score> scores = new HashSet<>();
                    score1.setPlayer(gamePlayer.getPlayer());
                    score1.setGame(gamePlayer.getGame());
                    score1.setFinishDate(LocalDateTime.now());
                    score1.setScore(1D);
                    scoreService.saveScore(score1);
                    scores.add(score1);

                    Score score2 = new Score();
                    score2.setPlayer(Util.enemyGamePlayer(gamePlayer).getPlayer());
                    score2.setGame(gamePlayer.getGame());
                    score2.setFinishDate(LocalDateTime.now());
                    score2.setScore(0D);
                    scoreService.saveScore(score2);
                    scores.add(score2);
                    Util.enemyGamePlayer(gamePlayer).getGame().setScores(scores);
                }
            }
            if(Util.setGameState(gamePlayer) == "TIE"){
                if(gamePlayer.getGame().getScores().size()<2) {
                    Set<Score> scores = new HashSet<Score>();
                    Score score1 = new Score();
                    score1.setPlayer(gamePlayer.getPlayer());
                    score1.setGame(gamePlayer.getGame());
                    score1.setFinishDate(LocalDateTime.now());
                    score1.setScore(0.5D);
                    scoreService.saveScore(score1);

                    Score score2 = new Score();
                    score2.setPlayer(Util.enemyGamePlayer(gamePlayer).getPlayer());
                    score2.setGame(gamePlayer.getGame());
                    score2.setFinishDate(LocalDateTime.now());
                    score2.setScore(0.5D);
                    scoreService.saveScore(score2);
                    scores.add(score1);
                    scores.add(score2);
                    Util.enemyGamePlayer(gamePlayer).getGame().setScores(scores);
                }

            }

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

        Player player = playerService.findPlayerByEmail(authentication.getName()).orElse(null);
        if(player == null) {
            return new ResponseEntity<>(Util.makeMap("error", "Player not found."), HttpStatus.NOT_FOUND);
        }

        Game game = gameService.findGameById(game_id).orElse(null);
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

        gamePlayerService.saveGamePlayer(gamePlayer);
        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);

    }







}
