package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.Service.dtos.NotificationDto;
import com.elmaguiri.backend.Service.dtos.RemarqueDto;
import com.elmaguiri.backend.Service.services.RemarqueOperationService;
import com.elmaguiri.backend.dao.entities.Notification;
import com.elmaguiri.backend.dao.repositories.NotificationRepository;
import com.elmaguiri.backend.serviceImp.NotificationService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService ;

    @Autowired
    public NotificationController( NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<NotificationDto> allNotification = notificationService.getAllNotification();
        return new ResponseEntity<>(allNotification, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> deleteNotifications() {
        notificationService.deleteNotifications();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
