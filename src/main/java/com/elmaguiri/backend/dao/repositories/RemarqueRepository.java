package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.EtapeOperation;
import com.elmaguiri.backend.dao.entities.RemarqueOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemarqueRepository extends JpaRepository<RemarqueOperation, Long> {
    List<RemarqueOperation> findByOperationId(Long operationId);

}
