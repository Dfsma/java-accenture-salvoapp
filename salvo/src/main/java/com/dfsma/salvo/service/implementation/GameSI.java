package com.dfsma.salvo.service.implementation;

import com.dfsma.salvo.models.Game;
import com.dfsma.salvo.repositories.GameRepository;
import com.dfsma.salvo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameSI implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Override
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    @Override
    public Game updateGame(Game game) {
        return null;
    }

    @Override
    public boolean deleteGame(Long id) {
        return false;
    }

    @Override
    public Optional<Game> findGameById(Long id) {
        return gameRepository.findById(id);
    }
}
