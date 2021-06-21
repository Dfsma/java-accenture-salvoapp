package com.dfsma.salvo.service.implementation;

import com.dfsma.salvo.models.Salvo;
import com.dfsma.salvo.repositories.SalvoRepository;
import com.dfsma.salvo.service.SalvoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalvoSI implements SalvoService {

    @Autowired
    SalvoRepository salvoRepository;
    @Override
    public Salvo saveSalvo(Salvo salvo) {
        return salvoRepository.save(salvo);
    }

    @Override
    public List<Salvo> getSalvos() {
        return salvoRepository.findAll();
    }

    @Override
    public Salvo updateSalvo(Salvo salvo) {
        return null;
    }

    @Override
    public boolean deleteSalvo(Long id) {
        return false;
    }

    @Override
    public Optional<Salvo> findSalvoById(Long id) {
        return salvoRepository.findById(id);
    }
}
