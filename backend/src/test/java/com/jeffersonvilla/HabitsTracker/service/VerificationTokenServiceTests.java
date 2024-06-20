package com.jeffersonvilla.HabitsTracker.service;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.INVALID_VERIFICATION_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jeffersonvilla.HabitsTracker.exceptions.VerificationTokenNotExistsException;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.model.VerificationToken;
import com.jeffersonvilla.HabitsTracker.repository.VerificationTokenRepo;
import com.jeffersonvilla.HabitsTracker.service.implementations.VerificationTokenServiceImpl;

import java.util.Optional;

public class VerificationTokenServiceTests {

    @Mock
    private VerificationTokenRepo verificationTokenRepo;

    @InjectMocks
    private VerificationTokenServiceImpl verificationTokenService;

    private User user;

    private VerificationToken verificationToken;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "testUser", "testUser@correo", "Password1*", false);
        verificationToken = new VerificationToken(1L, UUID.randomUUID().toString(), user);
    }

    @Test
    public void generateToken_success(){

        when(verificationTokenRepo.save(any(VerificationToken.class)))
            .thenReturn(verificationToken);

        VerificationToken actualVerificationToken = verificationTokenService.generateToken(user);

        assertNotNull(actualVerificationToken);
        assertNotNull(actualVerificationToken.getToken());
        assertNotNull(actualVerificationToken.getUser());

        assertEquals(verificationToken, actualVerificationToken);
        assertEquals(user, actualVerificationToken.getUser());

        verify(verificationTokenRepo).save(any(VerificationToken.class));
    }

    @Test
    public void verifyToken_tokenNotExists(){

        when(verificationTokenRepo.existsByToken(anyString())).thenReturn(false);

        VerificationTokenNotExistsException ex = assertThrows(VerificationTokenNotExistsException.class, ()->{
            verificationTokenService.verifyToken(anyString());
        });

        assertEquals(INVALID_VERIFICATION_TOKEN, ex.getMessage());

        verify(verificationTokenRepo, never()).findByToken(anyString());
    }

    @Test
    public void verifyToken_success(){

        when(verificationTokenRepo.existsByToken(anyString())).thenReturn(true);
        when(verificationTokenRepo.findByToken(anyString())).thenReturn(Optional.of(verificationToken));

        VerificationToken actualToken = verificationTokenService.verifyToken(anyString());

        assertNotNull(actualToken);
        assertNotNull(actualToken.getToken());
        assertNotNull(actualToken.getUser());

        assertEquals(verificationToken, actualToken);
        assertEquals(user, actualToken.getUser());

        verify(verificationTokenRepo, times(1)).findByToken(anyString());
    }
}
