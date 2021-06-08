package com.dfsma.salvo.service;

import com.dfsma.salvo.models.Player;

import java.util.Optional;

public interface PlayerService {

    Player findPlayerByUsername(String email);
}
