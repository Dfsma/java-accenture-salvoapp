package com.dfsma.salvo.dto;

import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Salvo;
import com.dfsma.salvo.util.Util;

import java.util.*;

public class hitDTO {

    public static List<Map> makeHitsDTO(GamePlayer gamePlayer){
        List<Map> hits = new LinkedList<>();

        int totalCarrierHits = 0;
        int totalBattleShipHits = 0;
        int totalSubmarineHits = 0;
        int totalDestroyerHits = 0;
        int totalPatrolBoatHits = 0;


        for (Salvo salvo : Util.enemyGamePlayer(gamePlayer).getSalvoes()){

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

                if(Util.getLocationsType("carrier", gamePlayer).contains(location)){
                    salvoHitList.add(location);
                    carrierHits++;
                    totalCarrierHits++;
                    missedHits--;

                }
                if(Util.getLocationsType("battleship", gamePlayer).contains(location)){
                    salvoHitList.add(location);
                    battleShipHits++;
                    totalBattleShipHits++;
                    missedHits--;

                }
                if(Util.getLocationsType("submarine", gamePlayer).contains(location)){
                    salvoHitList.add(location);
                    submarineHits++;
                    totalSubmarineHits++;
                    missedHits--;
                }
                if(Util.getLocationsType("destroyer", gamePlayer).contains(location)){
                    salvoHitList.add(location);
                    destroyerHits++;
                    totalDestroyerHits++;
                    missedHits--;
                }
                if(Util.getLocationsType("patrolboat", gamePlayer).contains(location)){
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

    public static int getDamage(GamePlayer gamePlayer) {

        int totalDamage = 0;

        for (Salvo salvo : Util.enemyGamePlayer(gamePlayer).getSalvoes()) {
            for (String location : salvo.getSalvoLocations()) {
                if (Util.getLocationsType("carrier", gamePlayer).contains(location)) {
                    totalDamage++;
                }
                if (Util.getLocationsType("battleship", gamePlayer).contains(location)) {
                    totalDamage++;
                }
                if (Util.getLocationsType("submarine", gamePlayer).contains(location)) {
                    totalDamage++;
                }
                if (Util.getLocationsType("destroyer", gamePlayer).contains(location)) {
                    totalDamage++;
                }
                if (Util.getLocationsType("patrolboat", gamePlayer).contains(location)) {
                    totalDamage++;
                }
            }
        }
        return totalDamage++;
    }
}
