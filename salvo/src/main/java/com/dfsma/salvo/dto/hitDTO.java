package com.dfsma.salvo.dto;

import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Salvo;
import com.dfsma.salvo.util.Util;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class hitDTO {

    public static List<Map> makeHitsDTO(GamePlayer gamePlayer){
        List<Map> hits = new LinkedList<>();

        Set<Salvo> salvo = Util.enemyGamePlayer(gamePlayer).getSalvoes();
        salvo.forEach(

                new Consumer<Salvo>() {
                    int totalCarrierHits = 0;
                    int totalBattleShipHits = 0;
                    int totalSubmarineHits = 0;
                    int totalDestroyerHits = 0;
                    int totalPatrolBoatHits = 0;

                @Override
                public void accept(Salvo salvo) {

                    Map<String, Object> dto = new LinkedHashMap<>();
                    Map<String, Object> damages = new LinkedHashMap<>();
                    List<String> salvoHitList = new ArrayList<>();


                    dto.put("turn", salvo.getTurn());
                    dto.put("hitLocations", salvoHitList);
                    salvo.getSalvoLocations().stream().forEach(

                            new Consumer<String>() {
                                int carrierHits = 0;
                                int battleShipHits = 0;
                                int submarineHits = 0;
                                int destroyerHits = 0;
                                int patrolBoatHits = 0;
                                int missedHits = salvo.getSalvoLocations().size();

                                   @Override
                                   public void accept(String s) {
                                       if(Util.getLocationsType("carrier", gamePlayer).contains(s)){
                                           salvoHitList.add(s);
                                           carrierHits++;
                                           totalCarrierHits++;
                                           missedHits--;
                                       }
                                       if(Util.getLocationsType("battleship", gamePlayer).contains(s)){
                                           salvoHitList.add(s);
                                           battleShipHits++;
                                           totalBattleShipHits++;
                                           missedHits--;
                                       }
                                       if(Util.getLocationsType("submarine", gamePlayer).contains(s)){
                                           salvoHitList.add(s);
                                           submarineHits++;
                                           totalSubmarineHits++;
                                           missedHits--;
                                       }
                                       if(Util.getLocationsType("destroyer", gamePlayer).contains(s)) {
                                           salvoHitList.add(s);
                                           destroyerHits++;
                                           totalDestroyerHits++;
                                           missedHits--;
                                       }
                                       if(Util.getLocationsType("patrolboat", gamePlayer).contains(s)){
                                           salvoHitList.add(s);
                                           patrolBoatHits++;
                                           totalPatrolBoatHits++;
                                           missedHits--;
                                       }
                                       damages.put("carrierHits", carrierHits);
                                       damages.put("battleshipHits", battleShipHits);
                                       damages.put("submarineHits", submarineHits);
                                       damages.put("destroyerHits", destroyerHits);
                                       damages.put("patrolboatHits", patrolBoatHits);

                                       dto.put("damages", damages);
                                       dto.put("missed", missedHits);
                                   }

                            });

                    damages.put("carrier", totalCarrierHits);
                    damages.put("battleship", totalBattleShipHits);
                    damages.put("submarine", totalSubmarineHits);
                    damages.put("destroyer", totalDestroyerHits);
                    damages.put("patrolboat", totalPatrolBoatHits);


                    hits.add(dto);
                }
        });
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
