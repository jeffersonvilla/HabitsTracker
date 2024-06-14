package com.jeffersonvilla.HabitsTracker.dto;

import org.junit.jupiter.api.Test;

import com.jeffersonvilla.HabitsTracker.Dto.RegisterUserRequestDto;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterUserRequestDtoTests {


    @Test
    public void testConstructorAndGetters() {
        // Test constructor
        RegisterUserRequestDto dto = new RegisterUserRequestDto("user123", "user@example.com", "password123");

        // Assert getters
        assertEquals("user123", dto.getUsername());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("password123", dto.getPassword());
    }

    @Test
    public void testSetters() {
        // Create RegisterUserRequestDto instance
        RegisterUserRequestDto dto = new RegisterUserRequestDto("user123", "user@example.com", "password123");

        // Use setters
        dto.setUsername("newuser");
        dto.setEmail("newuser@example.com");
        dto.setPassword("newpassword");

        // Assert changes
        assertEquals("newuser", dto.getUsername());
        assertEquals("newuser@example.com", dto.getEmail());
        assertEquals("newpassword", dto.getPassword());
    }

    @Test
    public void testToString() {
        // Create RegisterUserRequestDto instance
        RegisterUserRequestDto dto = new RegisterUserRequestDto("user123", "user@example.com", "password123");

        // Define expected toString output
        String expectedToString = "RegisterUserRequestDto [username=user123, email=user@example.com]";

        // Assert toString method
        assertEquals(expectedToString, dto.toString());
    }

}

