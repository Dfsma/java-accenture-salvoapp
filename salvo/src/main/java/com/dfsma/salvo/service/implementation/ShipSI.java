package com.dfsma.salvo.service.implementation;


import com.dfsma.salvo.models.Ship;
import com.dfsma.salvo.repositories.ShipRepository;
import com.dfsma.salvo.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipSI implements ShipService {

    @Autowired
    ShipRepository shipRepository;

    @Override
    public List<Ship> saveAllShips(Iterable<Ship> ships) {
        return shipRepository.saveAll(ships);
    }

    @Override
    public List<Ship> getShips() {
        return null;
    }

    @Override
    public Ship updateSalvo(Ship ship) {
        return null;
    }

    @Override
    public boolean deleteShip(Long id) {
        return false;
    }

    @Override
    public Optional<Ship> findShipById(Long id) {
        return Optional.empty();
    }
}
