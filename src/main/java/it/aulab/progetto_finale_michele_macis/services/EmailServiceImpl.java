package it.aulab.progetto_finale_michele_macis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendSimpleEmail(String to, String subject, String text){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@aulab.it");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            logger.info("Invio email a: {}, Subject: {}", to, subject);
            mailSender.send(message);
            logger.info("Email inviata con successo a: {}", to);
        } catch (Exception e) {
            logger.error("Errore durante l'invio email a: {}", to, e);
        }
    }
    
}
