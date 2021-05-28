package com.dfsma.salvo.controllers;

import com.dfsma.salvo.models.Game;
import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.repositories.GameRepository;
import com.dfsma.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    GameRepository gameRepository;

    @RequestMapping("/games")
    public List<Object> getAll(){
        //RETURN A LIST OF ALL GAMES AND CASCADE INFO FROM GAME CLASS(getGameAndPlayersInfo() method).
        return gameRepository.findAll().stream().map(Game::getGameAndPlayersInfo).collect(toList());
    }

















}
