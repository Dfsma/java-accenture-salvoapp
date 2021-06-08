package com.dfsma.salvo.service;


import com.dfsma.salvo.models.GamePlayer;

import java.util.List;

public interface GamePlayerService {

    GamePlayer saveGamePlayer(GamePlayer gamePlayer);
    List<GamePlayer> getGamePlayer();
    GamePlayer updateGamePlayer(GamePlayer gamePlayer);
    boolean deleteGamePlayer(Long id);
    GamePlayer findGamePlayerById(Long id);
}
