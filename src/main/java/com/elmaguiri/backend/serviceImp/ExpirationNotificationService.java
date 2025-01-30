package com.elmaguiri.backend.serviceImp;
import com.elmaguiri.backend.Service.dtos.ClientDto;
import com.elmaguiri.backend.Service.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ExpirationNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(ExpirationNotificationService.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * SUN")
    public void checkExpirations() {
        LocalDate today = LocalDate.now();
        List<ClientDto> clients = clientService.getAllClients();
        logger.info("Checking expirations...");
        for (ClientDto client : clients) {
            if (client.getEndDate() != null && client.getStatus()==true) {
                long daysUntilExpiration = ChronoUnit.DAYS.between(today, client.getEndDate());
                if (daysUntilExpiration <= 30 && daysUntilExpiration >= 0) {
                    logger.info("Sending expiration notification to client: {}", client.getId());
                    notificationService.sendExpirationNotification(client);
                }else if (daysUntilExpiration <= 0) {
                    logger.info("Sending expired notification to client: {}", client.getId());
                    notificationService.sendExpiredNotification(client);
                }
            }
        }
    }
}
