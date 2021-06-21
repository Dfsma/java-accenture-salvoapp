package com.dfsma.salvo.dto;

import com.dfsma.salvo.models.Score;

import java.util.LinkedHashMap;
import java.util.Map;

public class scoreDTO {


    public static Map<String, Object> makeScoreDto(Score score){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("score", score.getPlayer().getScorePlayer(score.getGame()).getScore());
        dto.put("player", score.getPlayer().getId());
        dto.put("finishDate", score.getFinishDate());
        return dto;
    }

}
