package com.dfsma.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import java.util.*;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {



}
