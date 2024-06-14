package com.jeffersonvilla.HabitsTracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.model.VerificationToken;
import com.jeffersonvilla.HabitsTracker.service.implementations.EmailServiceImpl;

public class EmailServiceImplTests {
    
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailServiceImpl;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_sendVerificationEmail(){

        User user = new User(1L, "testUsername", "test@email", "password", false);

        VerificationToken verificationToken = new VerificationToken(1L, "token123", user);

        emailServiceImpl.sendVerificationEmail(verificationToken);

        ArgumentCaptor<SimpleMailMessage> emailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        
        verify(mailSender, times(1)).send(emailCaptor.capture());
        SimpleMailMessage emailSent = emailCaptor.getValue();

        assertEquals(user.getEmail(), emailSent.getTo()[0]);
        assertEquals("Account Verification", emailSent.getSubject());
        assertEquals("Please click the following link to verify your account: http://localhost:8080/api/v1/user/verify?token=token123", emailSent.getText());
    }

}
