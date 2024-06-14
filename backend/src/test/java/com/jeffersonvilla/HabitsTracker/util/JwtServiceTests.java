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

    //Using secret key for testing purposes: 600e6e3c514e3f57e8c4860244e4505d9220f5eda8104b1c3446d3b41e3f19d8
    //private String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0VXNlcm5hb"
    //                +"WUiLCJpYXQiOjE3MTgyMzMyOTgsImV4cCI6MTcxODMxOTY5OH0.TRy4SF"
     //               +"__g9A0EOZelluhEM-gZdJ5YmlKJ0Zap9k0tWve1pM8cPMfefOLv_oOjvel";

    private String SECRET_KEY = "600e6e3c514e3f57e8c4860244e4505d9220f5eda8104b1c3446d3b41e3f19d8";

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_extractUsername(){
        String token = jwtService.generateToken(username);
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void test_extractExpiration(){
        String token = jwtService.generateToken(username);
        assertNotNull(jwtService.extractExpiration(token));
    }

    @Test
    void test_generateToken(){
        assertNotNull(jwtService.generateToken(username));
    }

    @Test
    void test_validateToken_valid(){
        String tokenGenerated = jwtService.generateToken(username);
        when(userDetails.getUsername()).thenReturn(username);
        assertTrue(jwtService.validateToken(tokenGenerated, userDetails));
    }

    @Test
    void test_validateToken_usernameNotvalid_ExpirationValid(){
        String tokenGenerated = jwtService.generateToken(username);
        when(userDetails.getUsername()).thenReturn("otherUseranme");
        assertFalse(jwtService.validateToken(tokenGenerated, userDetails));
    }

    @Test
    public void test_validateToken_usernameValid_tokenExpired() {

        String token = jwtService.generateToken(username);

        JwtService mockJwtService = mock(JwtService.class);

        when(userDetails.getUsername()).thenReturn(username);
        when(mockJwtService.extractExpiration(token)).thenReturn(new Date(System.currentTimeMillis() - 1000));

        assertFalse(mockJwtService.validateToken(token, userDetails));
    }

    @Test
    public void test_validateToken_usernameNotValid_tokenExpired() {

        String token = jwtService.generateToken(username);

        JwtService mockJwtService = mock(JwtService.class);

        when(userDetails.getUsername()).thenReturn("otherUsername");
        when(mockJwtService.extractExpiration(token)).thenReturn(new Date(System.currentTimeMillis() - 1000));

        assertFalse(mockJwtService.validateToken(token, userDetails));
    }

}
