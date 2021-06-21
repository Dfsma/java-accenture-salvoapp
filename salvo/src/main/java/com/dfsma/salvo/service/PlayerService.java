package com.dfsma.salvo.service;

import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    Player savePlayer(Player player);
    List<Player> getPlayers();
    Player updatePlayer(Player player);
    boolean deletePlayer(Long id);
    Optional<Player> findPlayerById(Long id);
    Optional<Player> findPlayerByEmail(String email);
}
