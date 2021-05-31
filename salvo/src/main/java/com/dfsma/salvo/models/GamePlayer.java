package com.dfsma.salvo.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

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

    public Set<Ship> getShip(){
        return ships;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public Map<String, Object> getGamePlayerInfo(){
        Map<String, Object> dto = new HashMap<String, Object>();
        dto.put("id", getId());
        dto.put("player", getPlayer().getPlayerInfo()); //PLAYER OBJECT{} (getPlayer()) , WHO CONTAINS DATA FROM PLAYER CLASS LIKE(ID,EMAIL)
        return dto;
    }
    public Map<String, Object> getGamePlayerShipsInfo(Ship ship){
        Map<String, Object> dto = new HashMap<>();
        //Game game = gamePlayer.getGame();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getShipLocations());

        return dto;
    }



    public void setShips(Set<Ship> ships) { this.ships = ships; }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }



}
