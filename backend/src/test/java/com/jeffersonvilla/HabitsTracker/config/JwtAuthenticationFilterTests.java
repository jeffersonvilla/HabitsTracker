package com.jeffersonvilla.HabitsTracker.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffersonvilla.HabitsTracker.exceptions.handler.ErrorResponse;
import com.jeffersonvilla.HabitsTracker.util.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.JWT_TOKEN_EXPIRED;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.JWT_TOKEN_NOT_VALID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class JwtAuthenticationFilterTests {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void test_MissingAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = mock(FilterChain.class);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify that filter chain proceeds without setting authentication
        assertEquals(null, SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void test_InvalidJwtToken() throws ServletException, IOException {
        // Mocking an invalid JWT token
        String invalidJwt = "invalid_jwt_token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + invalidJwt);
        MockHttpServletResponse response = new MockHttpServletResponse();

        String username = "testuser";
        UserDetails userDetails = mock(UserDetails.class);
        when(jwtService.extractUsername(invalidJwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(invalidJwt, userDetails)).thenReturn(false);

        FilterChain filterChain = mock(FilterChain.class);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify that no authentication was set in SecurityContextHolder
        assertEquals(null, SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void test_ValidJwtToken() throws ServletException, IOException {
        // Mocking a valid JWT token
        String validJwt = "valid_jwt_token";
        String username = "testuser";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + validJwt);
        MockHttpServletResponse response = new MockHttpServletResponse();

        UserDetails userDetails = mock(UserDetails.class);
        when(jwtService.extractUsername(validJwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(validJwt, userDetails)).thenReturn(true);

        FilterChain filterChain = mock(FilterChain.class);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify that authentication was set in SecurityContextHolder
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        assertEquals(userDetails, authToken.getPrincipal());
        assertEquals(null, authToken.getCredentials()); 

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void test_ExpiredJwtException() throws ServletException, IOException {

        String expiredJwt = "expired_jwt_token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + expiredJwt);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        
        when(jwtService.extractUsername(expiredJwt)).thenThrow(new ExpiredJwtException(null, null, "JWT expired"));
        
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        String expectedResponse = new ObjectMapper().writeValueAsString(new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), JWT_TOKEN_EXPIRED));
        assertEquals(expectedResponse, response.getContentAsString());
    }

    @Test
    void test_SignatureException() throws ServletException, IOException {

        String expiredJwt = "expired_jwt_token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + expiredJwt);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        
        // Arrange
        when(jwtService.extractUsername(anyString())).thenThrow(new SignatureException("JWT signature invalid"));

        // Act & Assert
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        String expectedResponse = new ObjectMapper().writeValueAsString(new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), JWT_TOKEN_NOT_VALID));
        assertEquals(expectedResponse, response.getContentAsString());
    }

    @Test
    void test_IllegalArgumentException() throws ServletException, IOException {

        String expiredJwt = "expired_jwt_token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + expiredJwt);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        
        // Arrange
        when(jwtService.extractUsername(anyString())).thenThrow(new IllegalArgumentException("JWT token compact of handler are invalid"));

        // Act & Assert
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        String expectedResponse = new ObjectMapper().writeValueAsString(new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), JWT_TOKEN_NOT_VALID));
        assertEquals(expectedResponse, response.getContentAsString());
    }
}

