package com.elmaguiri.backend.serviceImp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class mailService {

    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String fromEmailId;

    public mailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(fromEmailId);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
