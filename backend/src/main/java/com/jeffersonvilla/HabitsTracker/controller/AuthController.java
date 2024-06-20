package com.jeffersonvilla.HabitsTracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeffersonvilla.HabitsTracker.Dto.RegisterUserRequestDto;
import com.jeffersonvilla.HabitsTracker.Dto.LoginRequestDto;
import com.jeffersonvilla.HabitsTracker.service.interfaces.EmailService;
import com.jeffersonvilla.HabitsTracker.service.interfaces.AuthService;
import com.jeffersonvilla.HabitsTracker.service.interfaces.VerificationTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    public AuthController(AuthService userService, VerificationTokenService verificationTokenService
        , EmailService emailService){
        this.authService = userService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequestDto userDto){

        emailService.sendVerificationEmail(
            verificationTokenService.generateToken(
                authService.register(userDto)
            )
        );

        String successRegisterMessage = 
            "Thank you for registering! Please check your email to verify your account.";

        return new ResponseEntity<String>(successRegisterMessage, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token){
        
        authService.verifyUser(verificationTokenService.verifyToken(token).getUser());
        String message =  "Your email has been successfully verified! Welcome aboard!";
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto logingDto){
        String jwt = authService.login(logingDto);
        return new ResponseEntity<String>(jwt, HttpStatus.OK);

    }
}
