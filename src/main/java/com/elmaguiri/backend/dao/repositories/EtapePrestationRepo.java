package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.EtapePrestation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtapePrestationRepo extends JpaRepository<EtapePrestation, Long> {
    List<EtapePrestation> findByPrestationId(Long prestationId);
}
