package com.dfsma.salvo.controllers;


import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.repositories.GameRepository;
import com.dfsma.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class AppController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    GameRepository gameRepository;

    /*
    @RequestMapping("/games")
    public List<Object> getAll(){
        //RETURN A LIST OF ALL GAMES AND CASCADE INFO FROM GAME CLASS(getGameAndPlayersInfo() method).
        return gameRepository.findAll().stream().map(Game::getGameAndPlayersInfo).collect(toList());
    }
    */



















}
