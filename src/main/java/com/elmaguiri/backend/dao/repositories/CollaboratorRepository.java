package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    // You can add custom methods specific to Collaborator entity if needed
}