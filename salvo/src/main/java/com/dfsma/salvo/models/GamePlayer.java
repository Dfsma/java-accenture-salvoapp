package com.dfsma.salvo.models;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date joined;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvos;

    public GamePlayer() {

    }

    public GamePlayer(Player player, Game game) {
        this.joined = new Date();
        this.game = game;
        this.player = player;
        ships = new HashSet<Ship>();
    }

    public long getId() {
        return id;
    }

    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Ship> getShip(){ return ships; }
    public void setShips(Set<Ship> ships) { this.ships = ships; }

    public Set<Salvo> getSalvos() { return salvos; }
    public void setSalvos(Set<Salvo> salvos) { this.salvos = salvos; }


    public Map<String, Object> getGamePlayerInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        Score scr = getPlayer().getScorePlayer(getGame());
        dto.put("id", getId());
        dto.put("player", getPlayer().getPlayerInfo());
        if ( scr == null) {
            dto.put("score", null);
        }else{
            dto.put("score", scr.getScore());
        }
        return dto;
    }

    public Map<String, Object> getPlayerScoreInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId());
        dto.put("player", getPlayer().getPlayerInfo());
        dto.put("score", getPlayer().getScorePlayer(getGame()).getScore());
        return dto;
    }


    public List<Salvo> getSalvo(){return new ArrayList<>(this.salvos);}

    public Score getScore(double score){
        return new Score(score, player, game);
    }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvos.add(salvo);
    }

}
