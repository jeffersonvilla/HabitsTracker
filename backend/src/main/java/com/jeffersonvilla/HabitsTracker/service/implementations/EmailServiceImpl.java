package com.jeffersonvilla.HabitsTracker.service.implementations;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jeffersonvilla.HabitsTracker.model.VerificationToken;
import com.jeffersonvilla.HabitsTracker.service.interfaces.EmailService;

@Service
public class EmailServiceImpl implements EmailService{

    private JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(VerificationToken verficationToken) {
        String recipientAddress = verficationToken.getUser().getEmail();
        String subject = "Account Verification";
        String verificationUrl = "http://localhost:8080/api/v1/user/verify?token=" + verficationToken.getToken();

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Please click the following link to verify your account: " + verificationUrl);
        mailSender.send(email);
    }
    
}
