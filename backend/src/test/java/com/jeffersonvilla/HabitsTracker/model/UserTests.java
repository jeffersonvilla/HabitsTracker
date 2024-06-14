package com.jeffersonvilla.HabitsTracker.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {

    @Test
    public void testConstructorAndGetters() {
        // Test constructor
        User user = new User(1L, "john_doe", "john@example.com", "password123", true);

        // Assert getters
        assertEquals(1L, user.getId());
        assertEquals("john_doe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isVerified());
    }

    @Test
    public void testSetters() {
        // Create User instance
        User user = new User(null, null, null, null, false);

        // Set values using setters
        user.setId(2L);
        user.setUsername("jane_smith");
        user.setEmail("jane@example.com");
        user.setPassword("secret456");
        user.setVerified(true);

        // Assert changes
        assertEquals(2L, user.getId());
        assertEquals("jane_smith", user.getUsername());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("secret456", user.getPassword());
        assertTrue(user.isVerified());
    }

    @Test
    public void testToString() {
        // Create User instance
        User user = new User(3L, "mary_jones", "mary@example.com", "pass123", true);

        // Test toString method
        String expectedString = "User [id=3, username=mary_jones, email=mary@example.com, password=pass123, verified=true]";
        assertEquals(expectedString, user.toString());
    }

    @Test
    public void testUserDetailsMethods() {
        // Create User instance
        User user = new User(4L, "sam_brown", "sam@example.com", "qwerty", true);

        // Test UserDetails methods
        assertEquals("qwerty", user.getPassword());
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertNull(user.getAuthorities()); 
    }
}

