package com.dfsma.salvo.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "gamePlayers"})
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime date;


    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;


    public Game() {

    }

    public Game(LocalDateTime date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public List<Player> getPlayers() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }

    public List<GamePlayer> getGamePlayers(){
        return new ArrayList<>(this.gamePlayers);
    }


    public Map<String, Object> getGameAndPlayersInfo(){ //PRINCIPAL DATA FOR RUTE /API/GAMES
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId()); //GAME ID
        dto.put("created", getDate()); //DATE CREATION
        dto.put("gamePlayers", getGamePlayers().stream().map(gamePlayer -> gamePlayer.getGamePlayerInfo()).collect(toList())); //GAME PLAYERS ARRAY WHO CONTAINS AN ARRAY[] OF PLAYER INFO OBJECT{} -> SEE GAME PLAYER CLASS.
        /*dto.put("gamePlayers", getGamePlayers().stream().map(GamePlayer::getGamePlayerInfo).collect(toList())); //OTHER WAY */
        return dto;

    }


    @Override
    public String toString() {
        return "Game" + id + "[" + date + "]";
    }
}
