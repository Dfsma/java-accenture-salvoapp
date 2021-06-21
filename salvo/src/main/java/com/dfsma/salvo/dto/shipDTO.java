package com.dfsma.salvo.dto;

import com.dfsma.salvo.models.Ship;

import java.util.LinkedHashMap;
import java.util.Map;

public class shipDTO {

    public static Map<String, Object> getShipsInfo(Ship ship){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getShipLocations());
        return dto;
    }
}
