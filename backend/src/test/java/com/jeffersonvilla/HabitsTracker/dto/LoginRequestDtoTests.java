package com.jeffersonvilla.HabitsTracker.dto;

import org.junit.jupiter.api.Test;

import com.jeffersonvilla.HabitsTracker.Dto.LoginRequestDto;

import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestDtoTests {

    @Test
    public void testConstructorAndGetters() {
        // Test constructor
        LoginRequestDto loginRequestDto = new LoginRequestDto("user123", "user@example.com", "password123");

        // Assert getters
        assertEquals("user123", loginRequestDto.getUsername());
        assertEquals("user@example.com", loginRequestDto.getEmail());
        assertEquals("password123", loginRequestDto.getPassword());
    }

    @Test
    public void testSetters() {
        // Create LoginRequestDto instance
        LoginRequestDto loginRequestDto = new LoginRequestDto("user123", "user@example.com", "password123");

        // Use setters
        loginRequestDto.setUsername("newUser");
        loginRequestDto.setEmail("new@example.com");
        loginRequestDto.setPassword("newPassword");

        // Assert changes
        assertEquals("newUser", loginRequestDto.getUsername());
        assertEquals("new@example.com", loginRequestDto.getEmail());
        assertEquals("newPassword", loginRequestDto.getPassword());
    }
}

