package com.dfsma.salvo.models;



import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;



@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String email;
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Player() {}

    public Player(String email, String password) {

        this.email = email;
        this.password = password;
        this.gamePlayers = new HashSet<>();
        this.scores = new HashSet<>();
    }


    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<Score> getScores() {
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
        dto.put("id", getId());
        dto.put("email", getEmail());
        return dto;
    }

    public Score getScorePlayer(Game game){
        return scores.stream().filter(score -> score.getGame() == game).findFirst().orElse(null);
    }

    public double getWonScore(){
        return this.getScores().stream().
                filter(score -> score.getScore() == 1.0).count();
    }

    public double getLostScore(){
        return this.getScores().stream().
                filter(score -> score.getScore() == 0.0).count();
    }

    public double getTiedScore(){
        return this.getScores().stream().
                filter(score -> score.getScore() == 0.5).count();
    }

    public double getTotalScore(){
        return  getWonScore() + getLostScore() + getTiedScore() ;
    }



}
