package com.dfsma.salvo.service.implementation;

import com.dfsma.salvo.models.Score;
import com.dfsma.salvo.repositories.ScoreRepository;
import com.dfsma.salvo.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreSI implements ScoreService {

    @Autowired
    ScoreRepository scoreRepository;

    @Override
    public Score saveScore(Score score) {
        return scoreRepository.save(score);
    }
}
