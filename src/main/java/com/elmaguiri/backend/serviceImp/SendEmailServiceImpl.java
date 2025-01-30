package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.services.SendEmailService;
import com.elmaguiri.backend.dao.entities.Document;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SendEmailServiceImpl implements SendEmailService {
   // private DocumentServiceImpl documentService;
    private JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();


    public SendEmailServiceImpl(JavaMailSenderImpl mailSender)
    {
         this.javaMailSenderImpl = mailSender;
    }



    @Override
    public void sendEmail(String email, String subject,String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        message.setSentDate(new Date());
        message.setFrom("pfe16072024@gmail.com");

       // javaMailSenderImpl.send(message);
    }
    public void sendEmailWithAttachment(String email, String subject, String body, List<Document> documentList ) {
        try {
        // Create a MimeMessage
            MimeMessage message = javaMailSenderImpl.createMimeMessage();

        // Use MimeMessageHelper for multipart email (attachments)
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body);
        helper.setSentDate(new Date());
        helper.setFrom("pfe16072024@gmail.com");

            for (Document document : documentList) {
                byte[] fileData = document.getDocument();
                String fileName = document.getFileName();
                String contentType = document.getContentType();

                DataSource dataSource = new ByteArrayDataSource(fileData, contentType);
                helper.addAttachment(fileName, dataSource);
            }
            // Send the email
        javaMailSenderImpl.send(message);
        } catch (MessagingException e) {
            // Log the error or rethrow it as a runtime exception
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
