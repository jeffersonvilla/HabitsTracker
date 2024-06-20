package com.jeffersonvilla.HabitsTracker.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jeffersonvilla.HabitsTracker.Dto.LoginRequestDto;
import com.jeffersonvilla.HabitsTracker.Dto.RegisterUserRequestDto;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.model.VerificationToken;
import com.jeffersonvilla.HabitsTracker.service.implementations.VerificationTokenServiceImpl;
import com.jeffersonvilla.HabitsTracker.service.interfaces.AuthService;
import com.jeffersonvilla.HabitsTracker.service.interfaces.EmailService;

public class AuthControllerTests {
    
    @Mock
    private AuthService authService;

    @Mock
    private VerificationTokenServiceImpl verificationTokenService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_register(){

        RegisterUserRequestDto registerUserRequestDto = new RegisterUserRequestDto(
            "testUsername", "test@email", "password");

        ResponseEntity<String> response = authController.register(registerUserRequestDto);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @Test
    void test_verifyEmail(){
        String token = "Verification token";

        VerificationToken verificationToken = mock(VerificationToken.class);
        User user = mock(User.class);

        when(verificationTokenService.verifyToken(token)).thenReturn(verificationToken);
        when(verificationToken.getUser()).thenReturn(user);

        ResponseEntity<String> response = authController.verifyEmail(token);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @Test
    void test_login(){
        String jwt = "JWT token";

        LoginRequestDto loginRequestDto = new LoginRequestDto(
            "testUsername", "test@email", "password");

        when(authService.login(loginRequestDto)).thenReturn(jwt);

        ResponseEntity<String> response = authController.login(loginRequestDto);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(jwt, response.getBody());
    }
}
