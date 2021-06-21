package com.dfsma.salvo.dto;

import com.dfsma.salvo.models.Game;
import com.dfsma.salvo.models.GamePlayer;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class gameDTO {
    public static Map<String, Object> makeGameDTO(Game game){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> gamePlayerDTO.getGamePlayerInfo(gamePlayer)).collect(toList()));
        dto.put("scores", game.getScores().stream().map(score -> scoreDTO.makeScoreDto(score)).collect(toList()));
        return dto;
    }

}
