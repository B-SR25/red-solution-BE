package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

}