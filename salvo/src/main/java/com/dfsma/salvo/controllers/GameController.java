package com.dfsma.salvo.controllers;


import com.dfsma.salvo.models.*;

import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.repositories.GameRepository;
import com.dfsma.salvo.repositories.PlayerRepository;
import com.dfsma.salvo.service.GamePlayerService;
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

    @PostMapping("/games")
    public ResponseEntity<Object> createGame(Authentication authentication){
        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByEmail(authentication.getName());
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

            Player player = playerRepository.findByEmail(authentication.getName());


            GamePlayer gamePlayer = gamePlayerService.findGamePlayerById(gamePlayer_id);
            System.out.println(gamePlayer.getPlayer().getId());

            if(gamePlayer.getPlayer().getId() == player.getId()){
                return new ResponseEntity<>(this.makeGamePlayerDTO(gamePlayer), HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>(Util.makeMap("error", "You´re not in this game"), HttpStatus.NOT_FOUND);
            }



        }catch (Exception exception){
            return  new ResponseEntity<>("Error: El GamePlayer Id: " + gamePlayer_id + " no existe.",HttpStatus.BAD_REQUEST);
        }
    }


    //Join in a Game
    @PostMapping("/game/{game_id}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long game_id, Authentication authentication){

        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Not logged in."), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByEmail(authentication.getName());

        if(player == null) {
            return new ResponseEntity<>(Util.makeMap("error", "Player not found."), HttpStatus.NOT_FOUND);
        }

        Game game = gameRepository.findById(game_id).orElse(null);

        if(game == null){
            return new ResponseEntity<>(Util.makeMap("error", "Game" + game_id + "Not Found"), HttpStatus.NOT_FOUND);
        }

        List<Long> players_ids = game.getGamePlayers().stream().map(p -> p.getPlayer().getId()).collect(toList());
        System.out.println("Players ids:" + players_ids);

        if(players_ids.contains(player.getId())){
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
        dto.put("player", Util.isGuest(authentication) ? "Guest" : makePlayersDTO(playerRepository.findByEmail(authentication.getName())));
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


    public Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();
        hits.put("self", new ArrayList<>());
        hits.put("opponent", new ArrayList<>());

        Game game = gamePlayer.getGame();
        Set<Ship> ship = gamePlayer.getShip();


        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getJoined());
        dto.put("gameState", "PLACESHIPS");
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayers -> gamePlayers.getGamePlayerInfo()).collect(toList()));
        dto.put("ships", ship.stream().map(ships -> ships.getShipsInfo(ships)));
        dto.put("salvoes", game.getGamePlayers().stream().flatMap(gp -> gp.getSalvos().stream().map(salvo -> salvo.getSalvosInfo())).collect(toList()));
        dto.put("hits", hits);
        return dto;
    }


    public Map<String, Object> makePlayersDTO(Player player){

        Map<String, Object> dto = new HashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.getEmail());

        return dto;
    }





}
