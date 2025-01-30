package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.Enum.OperationState;
import com.elmaguiri.backend.dao.entities.Client;
import com.elmaguiri.backend.dao.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> , JpaSpecificationExecutor<Operation> {
    List<Operation> findByClient(Client client);

    @Query("SELECT o FROM Operation o JOIN o.client c WHERE c.status = true")
    List<Operation> findAllOperationsWithActiveClients();

    @Query("SELECT o FROM Operation o JOIN o.client c WHERE c.id = :clientId")
    List<Operation> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT COUNT(o) FROM Operation o WHERE o.statutOperation = :status")
    long countByStatus(@Param("status") OperationState status );


    @Query("SELECT o.statutOperation, COUNT(o) FROM Operation o GROUP BY o.statutOperation")
    List<Object[]> countAllByStatus();

    @Query("SELECT o.nameOperation FROM Operation o ORDER BY o.id DESC LIMIT 1")
    String findLastCode();


}
