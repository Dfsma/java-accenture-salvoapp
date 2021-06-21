package com.dfsma.salvo.service;


import com.dfsma.salvo.models.Salvo;

import java.util.List;
import java.util.Optional;

public interface SalvoService {

    Salvo saveSalvo(Salvo salvo);
    List<Salvo> getSalvos();
    Salvo updateSalvo(Salvo salvo);
    boolean deleteSalvo(Long id);
    Optional<Salvo> findSalvoById(Long id);


}
