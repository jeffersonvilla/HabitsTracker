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

import com.jeffersonvilla.HabitsTracker.Dto.UserDto;
import com.jeffersonvilla.HabitsTracker.service.interfaces.EmailService;
import com.jeffersonvilla.HabitsTracker.service.interfaces.UserService;
import com.jeffersonvilla.HabitsTracker.service.interfaces.VerificationTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    public UserController(UserService userService, VerificationTokenService verificationTokenService
        , EmailService emailService){
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto){

        emailService.sendVerificationEmail(
            verificationTokenService.generateToken(
                userService.register(userDto)
            )
        );

        String successRegisterMessage = 
            "Thank you for registering! Please check your email to verify your account.";

        return new ResponseEntity<String>(successRegisterMessage, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token){
        
        userService.verifyUser(verificationTokenService.verifyToken(token).getUser());
        String message =  "Your email has been successfully verified! Welcome aboard!";
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
}
