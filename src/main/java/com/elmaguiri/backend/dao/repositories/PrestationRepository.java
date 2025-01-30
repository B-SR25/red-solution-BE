package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.Prestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrestationRepository extends JpaRepository<Prestation, Long> {
    Optional<Prestation> findByName(String name);
    boolean existsById(Long id);
}
