package com.dfsma.salvo.models;


import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;


    public GamePlayer() {
        ships = new HashSet<Ship>();
    }

    public GamePlayer(Player player, Game game) {
        this.game = game;
        this.player = player;
        ships = new HashSet<Ship>();
    }

    public long getId() {
        return id;
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

    public Map<String, Object> getGamePlayerInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId()); // PLAYER ID
        dto.put("player", getPlayer().getPlayerInfo()); //PLAYER OBJECT{} (getPlayer()) , WHO CONTAINS DATA FROM PLAYER CLASS LIKE(ID,EMAIL)
        return dto;
    }

    public Set<Ship> getShips(){
        return ships;
    }
    public void setShips(Set<Ship> ships) { this.ships = ships; }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }





}
