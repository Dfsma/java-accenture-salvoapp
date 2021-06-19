package com.dfsma.salvo.controllers;


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

import java.time.Instant;
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
    ScoreRepository scoreRepository;

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
                GamePlayer enemyGamePlayer = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> (gp != gamePlayer)).findAny().orElse(null);

                if(setGameState(gamePlayer) == "WON"){
                    if (gamePlayer.getGame().getScores().size() < 2) {
                        Set<Score> scores = new HashSet<>();

                        Score score1 = new Score();
                        score1.setPlayer(gamePlayer.getPlayer());
                        score1.setGame(gamePlayer.getGame());
                        score1.setFinishDate(Date.from(Instant.now()));
                        score1.setScore(1D);
                        scoreRepository.save(score1);

                        Score score2 = new Score();
                        score2.setPlayer(enemyGamePlayer.getPlayer());
                        score2.setGame(gamePlayer.getGame());
                        score2.setFinishDate(Date.from(Instant.now()));
                        score2.setScore(0D);
                        scoreRepository.save(score2);

                        scores.add(score1);
                        scores.add(score2);
                        enemyGamePlayer.getGame().setScores(scores);

                    }
                }
                if(setGameState(gamePlayer) == "TIE"){
                    if(gamePlayer.getGame().getScores().size()<2) {
                        Set<Score> scores = new HashSet<Score>();

                        Score score1 = new Score();
                        score1.setPlayer(gamePlayer.getPlayer());
                        score1.setGame(gamePlayer.getGame());
                        score1.setFinishDate(Date.from(Instant.now()));
                        score1.setScore(0.5D);
                        scoreRepository.save(score1);

                        Score score2 = new Score();
                        score2.setPlayer(enemyGamePlayer.getPlayer());
                        score2.setGame(gamePlayer.getGame());
                        score2.setFinishDate(Date.from(Instant.now()));
                        score2.setScore(0.5D);
                        scoreRepository.save(score2);

                        scores.add(score1);
                        scores.add(score2);
                        enemyGamePlayer.getGame().setScores(scores);
                        }
                    }

                    return new ResponseEntity<>(this.makeGamePlayerDTO(gamePlayer), HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>(Util.makeMap("error", "You´re not in this game"), HttpStatus.NOT_FOUND);
            }

        }catch (Exception exception){
            return  new ResponseEntity<>("Error: El GamePlayer Id: " + gamePlayer_id + " no existe.", HttpStatus.BAD_REQUEST);
        }
    }


    //Join in a Game
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
        dto.put("player", Util.isGuest(authentication) ? "Guest" : makePlayersDTO(playerRepository.findByEmail(authentication.getName()).orElse(null)));
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

        GamePlayer enemyGamePlayer = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> (gp != gamePlayer)).findAny().orElse(null);
        if(gamePlayer.getGame().getGamePlayers().size() == 2 ){
            hits.put("self", makeHitsDTO(gamePlayer));
            hits.put("opponent", makeHitsDTO(enemyGamePlayer));
        }else{
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
        }

        Game game = gamePlayer.getGame();
        Set<Ship> ship = gamePlayer.getShips();

        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getJoined());
        dto.put("gameState", setGameState(gamePlayer));
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayers -> gamePlayers.getGamePlayerInfo()).collect(toList()));
        dto.put("ships", ship.stream().map(ships -> ships.getShipsInfo(ships)));
        dto.put("salvoes", game.getGamePlayers().stream().flatMap(gp -> gp.getSalvoes().stream().map(salvo -> salvo.getSalvosInfo())).collect(toList()));
        dto.put("hits", hits);
        return dto;
    }



    public List<String> getLocationsType(String type, GamePlayer gamePlayer){

        if(!(gamePlayer.getShips().size() == 0)){
            return gamePlayer.getShips().stream().filter(ship -> ship.getType().equals(type)).findFirst().get().getShipLocations();
        }

        return new ArrayList<>();
    }



    public List<Map> makeHitsDTO(GamePlayer gamePlayer){
            List<Map> hits = new LinkedList<>();

            int totalCarrierHits = 0;
            int totalBattleShipHits = 0;
            int totalSubmarineHits = 0;
            int totalDestroyerHits = 0;
            int totalPatrolBoatHits = 0;

            GamePlayer enemyGamePlayer = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> (gp != gamePlayer)).findAny().orElse(null);

            for (Salvo salvo : enemyGamePlayer.getSalvoes()){

                Map<String, Object> dto = new LinkedHashMap<>();
                Map<String, Object> damages = new LinkedHashMap<>();
                List<String> salvoHitList = new ArrayList<>();


                int carrierHits = 0;
                int battleShipHits = 0;
                int submarineHits = 0;
                int destroyerHits = 0;
                int patrolBoatHits = 0;
                int missedHits = salvo.getSalvoLocations().size();

                for (String location: salvo.getSalvoLocations()){

                    if(getLocationsType("carrier", gamePlayer).contains(location)){
                        salvoHitList.add(location);
                        carrierHits++;
                        totalCarrierHits++;
                        missedHits--;

                    }
                    if(getLocationsType("battleship", gamePlayer).contains(location)){
                        salvoHitList.add(location);
                        battleShipHits++;
                        totalBattleShipHits++;
                        missedHits--;

                    }
                    if(getLocationsType("submarine", gamePlayer).contains(location)){
                        salvoHitList.add(location);
                        submarineHits++;
                        totalSubmarineHits++;
                        missedHits--;
                    }
                    if(getLocationsType("destroyer", gamePlayer).contains(location)){
                        salvoHitList.add(location);
                        destroyerHits++;
                        totalDestroyerHits++;
                        missedHits--;
                    }
                    if(getLocationsType("patrolboat", gamePlayer).contains(location)){
                        salvoHitList.add(location);
                        patrolBoatHits++;
                        totalPatrolBoatHits++;
                        missedHits--;
                    }
                }

                damages.put("carrierHits", carrierHits);
                damages.put("battleshipHits", battleShipHits);
                damages.put("submarineHits", submarineHits);
                damages.put("destroyerHits", destroyerHits);
                damages.put("patrolboatHits", patrolBoatHits);

                damages.put("carrier", totalCarrierHits);
                damages.put("battleship", totalBattleShipHits);
                damages.put("submarine", totalSubmarineHits);
                damages.put("destroyer", totalDestroyerHits);
                damages.put("patrolboat", totalPatrolBoatHits);


                dto.put("turn", salvo.getTurn());
                dto.put("hitLocations", salvoHitList);
                dto.put("damages", damages);
                dto.put("missed", missedHits);
                hits.add(dto);
            }
            return hits;
    }

    public String setGameState(GamePlayer gamePlayer){

        GamePlayer enemyGamePlayer = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> (gp != gamePlayer)).findAny().orElse(null);
        if(gamePlayer.getGame().getGamePlayers().size()==2) {
            int myImpacts = makeDamage(gamePlayer);
            int enemyImpacts = makeDamage(enemyGamePlayer);

            if(myImpacts == 17 && enemyImpacts == 17){
                return  "TIE";
            }else if(myImpacts == 17 && gamePlayer.getSalvoes().size() == enemyGamePlayer.getSalvoes().size()){
                return "LOSE";
            }else if(enemyImpacts == 17 && gamePlayer.getSalvoes().size() == enemyGamePlayer.getSalvoes().size()){
                return "WON";
            }
        }
        if (gamePlayer.getShips().isEmpty()) {
            return "PLACESHIPS";
        }else if( (gamePlayer.getGame().getGamePlayers().size() == 1 ) || enemyGamePlayer.getShips().size() == 0 ){
            return "WAITINGFOROPP";
        }else if( gamePlayer.getGame().getGamePlayers().size()==2  && gamePlayer.getSalvoes().size() > enemyGamePlayer.getSalvoes().size()) {
            return "WAIT";
        }else{
            return "PLAY";
        }

    }

    public Map<String, Object> makePlayersDTO(Player player){

        Map<String, Object> dto = new HashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.getEmail());

        return dto;
    }

    public int makeDamage(GamePlayer gamePlayer) {
        GamePlayer enemyGamePlayer = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> (gp != gamePlayer)).findAny().orElse(null);

        int countImpact = 0;

        for (Salvo salvo : enemyGamePlayer.getSalvoes()) {
            for (String location : salvo.getSalvoLocations()) {
                if (getLocationsType("carrier", gamePlayer).contains(location)) {
                    countImpact++;
                }
                if (getLocationsType("battleship", gamePlayer).contains(location)) {
                    countImpact++;
                }
                if (getLocationsType("submarine", gamePlayer).contains(location)) {
                    countImpact++;
                }
                if (getLocationsType("destroyer", gamePlayer).contains(location)) {
                    countImpact++;
                }
                if (getLocationsType("patrolboat", gamePlayer).contains(location)) {
                    countImpact++;
                }
            }
        }
        return countImpact++;
    }



}
