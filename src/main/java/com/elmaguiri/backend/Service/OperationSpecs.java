package com.elmaguiri.backend.Service;

import com.elmaguiri.backend.Enum.OperationState;
import com.elmaguiri.backend.dao.entities.Operation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class OperationSpecs {

    public static Specification<Operation> hasId(Long providedId) {
        return (root, query, cb) -> cb.equal(root.get("id"), providedId);
    }

    public static Specification<Operation> containsName(String providedName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("nameOperation")),"%"+ providedName.toLowerCase() +"%");
    }

    public static Specification<Operation> hasClientName(String providedName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("client").get("title")),"%"+providedName.toLowerCase()+"%");
    }
    public static Specification<Operation> hasClientId(Long providedId) {
        return (root, query, cb) -> cb.equal(root.get("client").get("id"),providedId);
    }
    public static Specification<Operation> hasPrestationId(Long providedId) {
        return (root, query, cb) -> cb.equal(root.get("prestation").get("id"),providedId);
    }
    public static Specification<Operation> withinDateRange(Date[] dateRange) {
        if (dateRange == null || dateRange.length != 2) {
            throw new IllegalArgumentException("Date range must contain exactly two dates: start date and end date.");
        }

        Date startDate = dateRange[0];
        Date endDate = dateRange[1];

        return (root, query, cb) -> {
            Predicate startDatePredicate = cb.greaterThanOrEqualTo(root.get("operationDate"), startDate);
            Predicate endDatePredicate = cb.lessThanOrEqualTo(root.get("operationDate"), endDate);
            return cb.and(startDatePredicate, endDatePredicate);
        };
    }

    public static Specification<Operation> hasStatus(OperationState operationStatus) {
        return (root, query, cb) ->{
            try{
             return cb.equal(root.get("statutOperation"),operationStatus);
            }catch (IllegalArgumentException e){
                return cb.disjunction();
            }
        };

    }
}
