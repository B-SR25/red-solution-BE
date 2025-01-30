package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT d FROM Document d WHERE d.client.id = :clientId ORDER BY d.id ASC")
    List<Document> getDocumentsByClientId(@Param("clientId") Long clientId);
}

