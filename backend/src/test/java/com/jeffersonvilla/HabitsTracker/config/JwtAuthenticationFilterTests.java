package com.jeffersonvilla.HabitsTracker.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.jeffersonvilla.HabitsTracker.util.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}

