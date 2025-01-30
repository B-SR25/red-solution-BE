package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    User findByUsername(String username);
}
