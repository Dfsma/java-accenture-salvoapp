package com.dfsma.salvo.service;

import com.dfsma.salvo.models.Ship;

import java.util.List;
import java.util.Optional;

public interface ShipService {

    List<Ship> saveAllShips(Iterable<Ship> ships);
    List<Ship> getShips();
    Ship updateSalvo(Ship ship);
    boolean deleteShip(Long id);
    Optional<Ship> findShipById(Long id);

}
