package com.dfsma.salvo.service;


import com.dfsma.salvo.models.GamePlayer;

import java.util.List;
import java.util.Optional;

public interface GamePlayerService {

    GamePlayer saveGamePlayer(GamePlayer gamePlayer);
    List<GamePlayer> getGamePlayers();
    GamePlayer updateGamePlayer(GamePlayer gamePlayer);
    boolean deleteGamePlayer(Long id);
    Optional<GamePlayer> findGamePlayerById(Long id);
}
