package com.dfsma.salvo.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "shipLocations")
    private List<String> shipLocations;


    public enum ShipTypes {

        CARRIER("carrier"),
        BATTLESHIP("battleship"),
        SUBMARINE("submarine"),
        DESTROYER("destroyer"),
        PATROLBOAT("patrolboat");

        private final String getShipName;

        ShipTypes(String shipName) {
            getShipName = shipName;
        }
    }

    public Ship() {}

    public Ship(String type, GamePlayer gamePlayer, List<String> shipLocations) {
        this.type = type;
        this.gamePlayer = gamePlayer;
        this.shipLocations = shipLocations;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getShipLocations() { return shipLocations; }

    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }

    public Map<String, Object> getShipsInfo(Ship ship){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getShipLocations());
        return dto;
    }
}
