package com.jeffersonvilla.HabitsTracker.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import com.jeffersonvilla.HabitsTracker.exceptions.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;

import java.util.Optional;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MyUserDetailsServiceTests {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private User user;

    private String username = "testuser";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        //username = ;
        user = new User(1L, username, username+"@email",  "password", true);
    }

    @Test
    public void testLoadUserByUsername_UserFoundByUsername() {

        when(userRepo.findByUsernameOrEmail(username,username)).thenReturn(Optional.of(user));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());  
        verify(userRepo, times(1)).findByUsernameOrEmail(username, username);
    }

    @Test
    public void testLoadUserByUsername_UserFoundByEmail() {

        when(userRepo.findByUsernameOrEmail(user.getEmail(), user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getEmail());

        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword()); 
        verify(userRepo, times(1)).findByUsernameOrEmail(user.getEmail(), user.getEmail());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "nonexistentuser";

        when(userRepo.findByUsernameOrEmail(username, username)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> myUserDetailsService.loadUserByUsername(username)
        );

        assertEquals(USER_NOT_FOUND, exception.getMessage());
        verify(userRepo, times(1)).findByUsernameOrEmail(username, username);
    }
}

