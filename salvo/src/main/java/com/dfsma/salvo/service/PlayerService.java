package com.dfsma.salvo.service;

import com.dfsma.salvo.models.Player;

import java.util.List;

public interface PlayerService {

    Player savePlayer(Player player);
    List<Player> getPlayers();
    Player updatePlayer(Player player);
    boolean deletePlayer(Long id);
    Player findPlayerByEmail(String email);
}
