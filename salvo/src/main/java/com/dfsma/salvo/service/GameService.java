package com.dfsma.salvo.service;

import com.dfsma.salvo.models.Game;


import java.util.List;
import java.util.Optional;

public interface GameService {
    Game saveGame(Game game);
    List<Game> getGames();
    Game updateGame(Game game);
    boolean deleteGame(Long id);
    Optional<Game> findGameById(Long id);

}
