package com.jeffersonvilla.HabitsTracker.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VerificationTokenTests {

    @Test
    public void testConstructorAndGetters() {
        // Create a User instance
        User user = new User();

        // Test constructor
        VerificationToken verificationToken = new VerificationToken(1L, "token123", user);

        // Assert getters
        assertEquals(1L, verificationToken.getId());
        assertEquals("token123", verificationToken.getToken());
        assertEquals(user, verificationToken.getUser());
    }

    @Test
    public void testSetters() {
        // Create a User instance
        User user = new User();

        // Create VerificationToken instance
        VerificationToken verificationToken = new VerificationToken(null, null, null);

        // Use setters
        verificationToken.setId(2L);
        verificationToken.setToken("newToken456");
        verificationToken.setUser(user);

        // Assert changes
        assertEquals(2L, verificationToken.getId());
        assertEquals("newToken456", verificationToken.getToken());
        assertEquals(user, verificationToken.getUser());
    }

    @Test
    public void testToString() {
        // Create a User instance
        User user = new User();

        // Create VerificationToken instance
        VerificationToken verificationToken = new VerificationToken(3L, "token789", user);

        // Define expected toString output
        String expectedToString = "VerificationToken [id=3, token=token789, user=" + user.toString() + "]";

        // Assert toString method
        assertEquals(expectedToString, verificationToken.toString());
    }
}

