package com.elmaguiri.backend.dao.repositories;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.elmaguiri.backend.dao.entities.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByRc(Long rc);
    Optional<Client> findByTitle(String title);
    @Query("SELECT c FROM Client c WHERE c.status = :status")
    List<Client> findByStatus(@Param("status") Boolean status);
    @Query("SELECT c FROM Client c WHERE c.clientType = :clientType")
    List<Client> findByClientType(@Param("clientType") String clientType);

    @Query("SELECT c FROM Client c WHERE c.clientType = :clientType AND c.status = :status")
    List<Client> findByClientTypeAndStatus(@Param("clientType") String clientType,@Param("status") Boolean status);
}

