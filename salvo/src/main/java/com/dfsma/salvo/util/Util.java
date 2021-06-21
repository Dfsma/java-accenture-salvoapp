package com.dfsma.salvo.util;

import com.dfsma.salvo.dto.hitDTO;
import com.dfsma.salvo.models.GamePlayer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.dfsma.salvo.dto.hitDTO.getDamage;

public class Util {

    public static Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public static GamePlayer enemyGamePlayer(GamePlayer gamePlayer){
        GamePlayer enemyGamePlayer = gamePlayer.getGame().getGamePlayers().stream().filter(gp -> (gp != gamePlayer)).findAny().orElse(null);
        return  enemyGamePlayer;
    }

    public static List<String> getLocationsType(String type, GamePlayer gamePlayer){

        if(!(gamePlayer.getShips().size() == 0)){
            return gamePlayer.getShips().stream().filter(ship -> ship.getType().equals(type)).findFirst().get().getShipLocations();
        }

        return new ArrayList<>();
    }

    public static String setGameState(GamePlayer gamePlayer){

        if(gamePlayer.getGame().getGamePlayers().size()==2) {
            int myImpacts = hitDTO.getDamage(gamePlayer);
            int enemyImpacts = getDamage(Util.enemyGamePlayer(gamePlayer));

            if(myImpacts == 17 && enemyImpacts == 17){
                return  "TIE";
            }else if(myImpacts == 17 && gamePlayer.getSalvoes().size() == Util.enemyGamePlayer(gamePlayer).getSalvoes().size()){
                return "LOSE";
            }else if(enemyImpacts == 17 && gamePlayer.getSalvoes().size() == Util.enemyGamePlayer(gamePlayer).getSalvoes().size()){
                return "WON";
            }
        }
        if (gamePlayer.getShips().isEmpty()) {
            return "PLACESHIPS";
        }else if( (gamePlayer.getGame().getGamePlayers().size() == 1 ) || Util.enemyGamePlayer(gamePlayer).getShips().size() == 0 ){
            return "WAITINGFOROPP";
        }else if( gamePlayer.getGame().getGamePlayers().size()==2  && gamePlayer.getSalvoes().size() > Util.enemyGamePlayer(gamePlayer).getSalvoes().size()) {
            return "WAIT";
        }else{
            return "PLAY";
        }

    }
}
