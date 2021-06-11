package com.dfsma.salvo.repositories;

import com.dfsma.salvo.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.*;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByEmail(@Param("email") String email);


}
