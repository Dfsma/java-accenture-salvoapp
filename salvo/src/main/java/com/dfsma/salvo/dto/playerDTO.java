package com.dfsma.salvo.dto;

import com.dfsma.salvo.models.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class playerDTO {

    public static Map<String, Object> makePlayersDTO(Player player){

        Map<String, Object> dto = new HashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.getEmail());

        return dto;
    }

    public static Map<String, Object> makePlayersAndScoresDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> score = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.getEmail());
        score.put("total", player.getTotalScore());
        score.put("won", player.getWonScore());
        score.put("tied", player.getTiedScore());
        score.put("lost", player.getLostScore());
        dto.put("score", score);

        return dto;
    }

}
