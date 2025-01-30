package com.elmaguiri.backend.serviceImp;
import com.elmaguiri.backend.Service.dtos.ClientDto;
import com.elmaguiri.backend.Service.dtos.NotificationDto;
import com.elmaguiri.backend.Service.dtos.RemarqueDto;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.entities.Notification;
import com.elmaguiri.backend.dao.entities.RemarqueOperation;
import com.elmaguiri.backend.dao.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapperConfig modelMapperConfig;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendExpirationNotification(ClientDto client) {
        logger.info("Preparing to send notification for client: {}", client.getId());

        // Create a NotificationDto object
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage("le mandat de gérance pour  " + client.getTitle() + " expire dans moins de 30 jours.");
        Date currentDate = new Date();
        notificationDto.setNotificationDate(currentDate);
        notificationDto.setClientId(client.getId());

        // Save the notification in the database
        Notification notification = modelMapperConfig.fromNotificationDto(notificationDto);
        notificationRepository.save(notification);
        // Update the NotificationDto with the generated ID
        notificationDto.setId(notification.getId());

        // Send the NotificationDto object as JSON
        messagingTemplate.convertAndSend("/topic/notifications", notificationDto);

        logger.info("Sending notification: {}", notificationDto);
    }

    public void sendExpiredNotification(ClientDto client) {
        // Create a NotificationDto object
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage("le mandat de gérance pour  " + client.getTitle() + " est expiré.");
        Date currentDate = new Date();
        notificationDto.setNotificationDate(currentDate);
        notificationDto.setClientId(client.getId());

        // Save the notification in the database
        Notification notification = modelMapperConfig.fromNotificationDto(notificationDto);
        notificationRepository.save(notification);

        // Update the NotificationDto with the generated ID
        notificationDto.setId(notification.getId());

        // Send the NotificationDto object as JSON
        messagingTemplate.convertAndSend("/topic/notifications", notificationDto);

        logger.info("Sending notification: {}", notificationDto);
    }


    public String deleteNotification(Long id) {
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        if (notificationOptional.isPresent()) {
            notificationRepository.deleteById(id);
            return "notification with id: " + id + " has been deleted successfully.";
        } else {
            throw new RuntimeException("notification not found with id: " + id);
        }
    }

    public List<NotificationDto> getAllNotification() {
        return notificationRepository.findAll()
                .stream()
                .map(modelMapperConfig::fromNotification)
                .collect(Collectors.toList());
    }


    public void deleteNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        if (notifications.isEmpty()) {
            throw new RuntimeException("No notifications to delete.");
        }
        notificationRepository.deleteAll();
        logger.info("All notifications have been deleted successfully.");
    }

}


