package com.elmaguiri.backend.web.controller;
import com.elmaguiri.backend.Service.dtos.ClientDto;
import com.elmaguiri.backend.serviceImp.ExpirationNotificationService;
import com.elmaguiri.backend.serviceImp.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class TestNotification {

        private final ExpirationNotificationService expirationNotificationService;
        @Autowired
        private NotificationService notificationService;

        public TestNotification(ExpirationNotificationService expirationNotificationService) {
            this.expirationNotificationService = expirationNotificationService;
        }

        @GetMapping("/test-expiration-check")
        public String testExpirationCheck() {
            expirationNotificationService.checkExpirations();
            return "Expiration check executed";
        }

        @PostMapping("/test")
        public ResponseEntity<String> sendTestNotification() {
            ClientDto client = new ClientDto(); // Créez un ClientDto test
            client.setTitle("Test Client");
            notificationService.sendExpirationNotification(client);
            return ResponseEntity.ok("Notification sent");
        }
    }

