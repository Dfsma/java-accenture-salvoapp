package com.dfsma.salvo.service.implementation;

import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.repositories.PlayerRepository;
import com.dfsma.salvo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerSI implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public Player savePlayer(Player player) {return playerRepository.save(player);}

    @Override
    public List<Player> getPlayers() { return null; }

    @Override
    public Player updatePlayer(Player player) { return null; }

    @Override
    public boolean deletePlayer(Long id) { return false; }

    @Override
    public Player findPlayerByEmail(String email) { return playerRepository.findByEmail(email).orElse(null); }
}
