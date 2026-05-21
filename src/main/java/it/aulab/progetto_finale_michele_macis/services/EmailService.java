package it.aulab.progetto_finale_michele_macis.services;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
    
}
