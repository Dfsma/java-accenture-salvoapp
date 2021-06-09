package com.dfsma.salvo.repositories;


import com.dfsma.salvo.models.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface ShipRepository extends JpaRepository<Ship, Long> {

}
