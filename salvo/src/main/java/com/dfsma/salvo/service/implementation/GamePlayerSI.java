package com.dfsma.salvo.service.implementation;

import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.service.GamePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GamePlayerSI implements GamePlayerService {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Override
    public GamePlayer saveGamePlayer(GamePlayer gamePlayer) {
        return gamePlayerRepository.save(gamePlayer);
    }

    @Override
    public List<GamePlayer> getGamePlayers() {
        return null;
    }

    @Override
    public GamePlayer updateGamePlayer(GamePlayer gamePlayer) {
        return null;
    }

    @Override
    public boolean deleteGamePlayer(Long id) {
        return false;
    }
    @Override
    public Optional<GamePlayer> findGamePlayerById(Long id) {
        return gamePlayerRepository.findById(id);
    }
}
