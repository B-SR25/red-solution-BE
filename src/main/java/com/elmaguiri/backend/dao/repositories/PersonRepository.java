package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}

