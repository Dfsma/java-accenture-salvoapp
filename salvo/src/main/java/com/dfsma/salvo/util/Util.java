package com.dfsma.salvo.util;

import com.dfsma.salvo.dto.hitDTO;
import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Score;
import com.dfsma.salvo.service.GamePlayerService;
import com.dfsma.salvo.service.ScoreService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.dfsma.salvo.dto.hitDTO.getDamage;

public class Util {

    @Autowired
    static

    ScoreService scoreService;



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
                return "TIE";
            }else if(myImpacts == 17 && gamePlayer.getSalvoes().size() == Util.enemyGamePlayer(gamePlayer).getSalvoes().size()){
                return "LOST";
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

    public static void addScoreToFinishedGame(GamePlayer gamePlayer){

        if(setGameState(gamePlayer) == "WON"){
            if(gamePlayer.getGame().getScores().size()<2) {
                Score score1 = new Score();
                Set<Score> scores = new HashSet<>();
                score1.setPlayer(gamePlayer.getPlayer());
                score1.setGame(gamePlayer.getGame());
                score1.setFinishDate(LocalDateTime.now());
                score1.setScore(1D);
                scoreService.saveScore(score1);
                scores.add(score1);

                Score score2 = new Score();
                score2.setPlayer(enemyGamePlayer(gamePlayer).getPlayer());
                score2.setGame(gamePlayer.getGame());
                score2.setFinishDate(LocalDateTime.now());
                score2.setScore(0D);
                scoreService.saveScore(score2);
                scores.add(score2);
                enemyGamePlayer(gamePlayer).getGame().setScores(scores);
            }
        }
        if(setGameState(gamePlayer) == "TIE"){
            if(gamePlayer.getGame().getScores().size()<2) {
                Set<Score> scores = new HashSet<Score>();
                Score score1 = new Score();
                score1.setPlayer(gamePlayer.getPlayer());
                score1.setGame(gamePlayer.getGame());
                score1.setFinishDate(LocalDateTime.now());
                score1.setScore(0.5D);
                scoreService.saveScore(score1);

                Score score2 = new Score();
                score2.setPlayer(enemyGamePlayer(gamePlayer).getPlayer());
                score2.setGame(gamePlayer.getGame());
                score2.setFinishDate(LocalDateTime.now());
                score2.setScore(0.5D);
                scoreService.saveScore(score2);
                scores.add(score1);
                scores.add(score2);
                enemyGamePlayer(gamePlayer).getGame().setScores(scores);
            }

        }

    }

}
