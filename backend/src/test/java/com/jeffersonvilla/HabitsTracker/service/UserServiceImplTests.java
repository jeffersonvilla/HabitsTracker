package com.jeffersonvilla.HabitsTracker.service;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.EMAIL_IN_USE_MESSAGE;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.PASSWORD_FORMAT_EXCEPTION_MESSAGE;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USERNAME_IN_USE_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jeffersonvilla.HabitsTracker.Dto.RegisterUserRequestDto;
import com.jeffersonvilla.HabitsTracker.exceptions.EmailInUseException;
import com.jeffersonvilla.HabitsTracker.exceptions.PasswordFormatException;
import com.jeffersonvilla.HabitsTracker.exceptions.UsernameInUseException;
import com.jeffersonvilla.HabitsTracker.mapper.Mapper;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;
import com.jeffersonvilla.HabitsTracker.service.implementations.AuthServiceImpl;
import com.jeffersonvilla.HabitsTracker.validation.Validator;


public class UserServiceImplTests {

    @Mock
    private UserRepo userRepo;

    @Mock
    private Mapper<User, RegisterUserRequestDto> mapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private Validator<String> passwordValidator;

    @InjectMocks
    private AuthServiceImpl userService;

    private RegisterUserRequestDto userDto;

    private User user;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        userDto = new RegisterUserRequestDto("testUser", "testUser@correo", "Password1*");
        user = new User(1L, "testUser", "testUser@correo", "encodedPassword", false);
    }
    
    @Test
    public void userRegister_usernameExists_shouldThrow_UsernameInUseException(){

        when(userRepo.existsByUsername(userDto.getUsername())).thenReturn(true);
        
        UsernameInUseException ex = assertThrows(UsernameInUseException.class, ()->{
            userService.register(userDto);
        });

        assertEquals(USERNAME_IN_USE_MESSAGE, ex.getMessage());

        verify(userRepo, never()).existsByEmail(anyString());
        verify(passwordValidator, never()).validate(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void userRegister_emailExists_shouldThrow_EmailInUseException(){

        when(userRepo.existsByUsername(userDto.getUsername())).thenReturn(false);

        when(userRepo.existsByEmail(userDto.getEmail())).thenReturn(true);

        EmailInUseException ex = assertThrows(EmailInUseException.class, ()->{
            userService.register(userDto);
        });

        assertEquals(EMAIL_IN_USE_MESSAGE, ex.getMessage());

        verify(passwordValidator, never()).validate(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void userRegister_passwordFormatInvalid_shouldThrow_PasswordFormatException(){

        when(userRepo.existsByUsername(userDto.getUsername())).thenReturn(false);

        when(userRepo.existsByEmail(userDto.getEmail())).thenReturn(false);

        doThrow(new PasswordFormatException(PASSWORD_FORMAT_EXCEPTION_MESSAGE))
            .when(passwordValidator).validate(userDto.getPassword());

        PasswordFormatException ex = assertThrows(PasswordFormatException.class, ()->{
            userService.register(userDto);
        });

        assertEquals(PASSWORD_FORMAT_EXCEPTION_MESSAGE, ex.getMessage());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void userRegister_successful() {

        String plainPassword = userDto.getPassword();
        String encondedPassword = "encoded";

        when(userRepo.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepo.existsByEmail(userDto.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(anyString())).thenReturn(encondedPassword);
        when(mapper.fromDto(userDto)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(user);

        User registeredUser = userService.register(userDto);

        assertNotNull(registeredUser);
        assertEquals(user.getUsername(), registeredUser.getUsername());
        assertEquals(user.getEmail(), registeredUser.getEmail());
        assertEquals(user.getPassword(), registeredUser.getPassword());
        verify(passwordValidator, times(1)).validate(anyString());
        verify(passwordEncoder, times(1)).encode(plainPassword);
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void testVerifyUser_success() {
        userService.verifyUser(user);

        assertTrue(user.isVerified());
        verify(userRepo).save(user);
    }


}
