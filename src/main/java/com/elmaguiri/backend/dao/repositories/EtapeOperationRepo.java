package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.EtapeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtapeOperationRepo extends JpaRepository<EtapeOperation, Long> {
    List<EtapeOperation> findByOperationId(Long operationId);
}
