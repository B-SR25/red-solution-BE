package com.elmaguiri.backend.dao.repositories;

import com.elmaguiri.backend.dao.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByClientId(Long clientId);
}
