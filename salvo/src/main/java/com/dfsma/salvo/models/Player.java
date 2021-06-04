package com.dfsma.salvo.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "gamePlayers"})
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName;
    private String email;



    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    public Player() {}

    public Player(String userName, String email) {
        this.userName = userName;
        this.email = email;
        this.gamePlayers = new HashSet<>();
        this.scores = new HashSet<>();
    }

    /*Getter y Setters*/
    public long getId() {
        return id;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<Score> getScore() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);

    }
    public void addScore(Score score){
        score.setPlayer(this);
        scores.add(score);
    }

    public Map<String, Object> getPlayerInfo(){
        Map<String, Object> dto = new HashMap<String, Object>();
        dto.put("id", getId()); //PLAYER ID
        dto.put("email", getEmail()); // PLAYER EMAIL
        return dto;
    }

    public Score getScorePlayer(Game game){
        return scores.stream().filter(s -> s.getGame() == game).findFirst().orElse(null);
    }


    @Override
    public String toString() {
        return "Player{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
