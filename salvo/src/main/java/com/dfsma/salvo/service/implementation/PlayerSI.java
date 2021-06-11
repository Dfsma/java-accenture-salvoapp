package com.dfsma.salvo.service.implementation;

import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.repositories.PlayerRepository;
import com.dfsma.salvo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerSI implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public Player findPlayerByUsername(String email) {
        return playerRepository.findByEmail(email).orElse(null);
    }
}
