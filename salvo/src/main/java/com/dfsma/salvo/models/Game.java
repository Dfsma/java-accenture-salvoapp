package com.dfsma.salvo.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime date = LocalDateTime.now();


    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();


    public Game() {
        this.gamePlayers = new HashSet<>();
        this.scores = new HashSet<>();
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


    public List<GamePlayer> getGamePlayers(){ return new ArrayList<>(this.gamePlayers); }

    public List<Player> getPlayers(){
        return  getGamePlayers().stream().map(p -> p.getPlayer()).collect(Collectors.toList());
    }

    public void addScore(Score score){
        score.setGame(this);
        scores.add(score);
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public boolean addGameScore(Score score){
        return  scores.add(score);
    }


}
