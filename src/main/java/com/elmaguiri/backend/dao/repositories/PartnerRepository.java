package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.Client;
import com.elmaguiri.backend.dao.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findByClientId(Long clientId);
}