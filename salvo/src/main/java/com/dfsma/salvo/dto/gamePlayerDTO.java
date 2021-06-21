package com.dfsma.salvo.dto;

import com.dfsma.salvo.models.*;
import com.dfsma.salvo.util.Util;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class gamePlayerDTO {

    public static Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();

        GamePlayer enemyGamePlayer = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> (gp != gamePlayer)).findAny().orElse(null);
        if(gamePlayer.getGame().getGamePlayers().size() == 2 ){
            hits.put("self",  hitDTO.makeHitsDTO(gamePlayer));
            hits.put("opponent",  hitDTO.makeHitsDTO(enemyGamePlayer));
        }else{
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
        }

        Game game = gamePlayer.getGame();
        Set<Ship> ship = gamePlayer.getShips();

        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getJoined());
        dto.put("gameState", Util.setGameState(gamePlayer));
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayers -> getGamePlayerInfo(gamePlayers)).collect(toList()));
        dto.put("ships", ship.stream().map(ships -> shipDTO.getShipsInfo(ships)));
        dto.put("salvoes", game.getGamePlayers().stream().flatMap(gp -> gp.getSalvoes().stream().map(salvo -> salvo.getSalvosInfo())).collect(toList()));
        dto.put("hits", hits);
        return dto;
    }

    public static Map<String, Object> getGamePlayerInfo(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        Score scr = gamePlayer.getPlayer().getScorePlayer(gamePlayer.getGame());
        dto.put("id", gamePlayer.getId());
        dto.put("player", gamePlayer.getPlayer().getPlayerInfo());
        if ( scr == null) {
            dto.put("score", 0);
        }else{
            dto.put("score", scr.getScore());
        }
        return dto;
    }



}
