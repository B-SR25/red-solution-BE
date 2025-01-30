package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f.code FROM File f ORDER BY f.id DESC LIMIT 1")
    String findLastCode();
}