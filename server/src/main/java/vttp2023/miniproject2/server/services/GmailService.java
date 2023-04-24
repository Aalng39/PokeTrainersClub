package vttp2023.miniproject2.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class GmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(
      String toEmail, String subject, String text) {
        
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("naitsabeslovecheese@gmail.com");
        message.setTo(toEmail); 
        message.setSubject(subject); 
        message.setText(text);

        emailSender.send(message);
        System.out.println("Mail Send...");
        
    }
}
