package com.jeffersonvilla.HabitsTracker.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public class JwtServiceTests {
    
    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String username = "testUsername";
    private Long id = 1L;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_extractUsername(){
        String token = jwtService.generateToken(username, id);
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void test_extractExpiration(){
        String token = jwtService.generateToken(username, id);
        assertNotNull(jwtService.extractExpiration(token));
    }

    @Test
    void test_generateToken(){
        assertNotNull(jwtService.generateToken(username, id));
    }

    @Test
    void test_validateToken_valid(){
        String tokenGenerated = jwtService.generateToken(username, id);
        when(userDetails.getUsername()).thenReturn(username);
        assertTrue(jwtService.validateToken(tokenGenerated, userDetails));
    }

    @Test
    void test_validateToken_usernameNotvalid_ExpirationValid(){
        String tokenGenerated = jwtService.generateToken(username, id);
        when(userDetails.getUsername()).thenReturn("otherUseranme");
        assertFalse(jwtService.validateToken(tokenGenerated, userDetails));
    }

    @Test
    public void test_validateToken_usernameValid_tokenExpired() {

        String token = jwtService.generateToken(username, id);

        JwtService mockJwtService = mock(JwtService.class);

        when(userDetails.getUsername()).thenReturn(username);
        when(mockJwtService.extractExpiration(token)).thenReturn(new Date(System.currentTimeMillis() - 1000));

        assertFalse(mockJwtService.validateToken(token, userDetails));
    }

    @Test
    public void test_validateToken_usernameNotValid_tokenExpired() {

        String token = jwtService.generateToken(username, id);

        JwtService mockJwtService = mock(JwtService.class);

        when(userDetails.getUsername()).thenReturn("otherUsername");
        when(mockJwtService.extractExpiration(token)).thenReturn(new Date(System.currentTimeMillis() - 1000));

        assertFalse(mockJwtService.validateToken(token, userDetails));
    }

}
