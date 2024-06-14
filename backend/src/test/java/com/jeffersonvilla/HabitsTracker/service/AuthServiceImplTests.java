package com.jeffersonvilla.HabitsTracker.service;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.EMAIL_IN_USE_MESSAGE;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.INVALID_PASSWORD;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.NEEDED_USERNAME_OR_EMAIL;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.PASSWORD_FORMAT_EXCEPTION_MESSAGE;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USERNAME_IN_USE_MESSAGE;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_FOUND;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jeffersonvilla.HabitsTracker.Dto.LoginRequestDto;
import com.jeffersonvilla.HabitsTracker.Dto.RegisterUserRequestDto;
import com.jeffersonvilla.HabitsTracker.exceptions.EmailInUseException;
import com.jeffersonvilla.HabitsTracker.exceptions.InvalidLoginCredentialsException;
import com.jeffersonvilla.HabitsTracker.exceptions.PasswordFormatException;
import com.jeffersonvilla.HabitsTracker.exceptions.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.UsernameInUseException;
import com.jeffersonvilla.HabitsTracker.mapper.Mapper;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;
import com.jeffersonvilla.HabitsTracker.service.implementations.AuthServiceImpl;
import com.jeffersonvilla.HabitsTracker.util.JwtService;
import com.jeffersonvilla.HabitsTracker.validation.Validator;

import java.util.Optional;

public class AuthServiceImplTests {

    @Mock
    private UserRepo userRepo;

    @Mock
    private Mapper<User, RegisterUserRequestDto> mapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private Validator<String> passwordValidator;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterUserRequestDto registerUserRequestDto;

    private User user;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        registerUserRequestDto = new RegisterUserRequestDto(
            "testUser", 
            "testUser@correo", 
            "Password1*"
            );        
        
        user = new User(1L, "testUser", "testUser@correo", "encodedPassword", false);
    }
    
    @Test
    public void userRegister_usernameExists_shouldThrow_UsernameInUseException(){

        when(userRepo.existsByUsername(registerUserRequestDto.getUsername())).thenReturn(true);
        
        UsernameInUseException ex = assertThrows(UsernameInUseException.class, ()->{
            authService.register(registerUserRequestDto);
        });

        assertEquals(USERNAME_IN_USE_MESSAGE, ex.getMessage());

        verify(userRepo, never()).existsByEmail(anyString());
        verify(passwordValidator, never()).validate(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void userRegister_emailExists_shouldThrow_EmailInUseException(){

        when(userRepo.existsByUsername(registerUserRequestDto.getUsername())).thenReturn(false);

        when(userRepo.existsByEmail(registerUserRequestDto.getEmail())).thenReturn(true);

        EmailInUseException ex = assertThrows(EmailInUseException.class, ()->{
            authService.register(registerUserRequestDto);
        });

        assertEquals(EMAIL_IN_USE_MESSAGE, ex.getMessage());

        verify(passwordValidator, never()).validate(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void userRegister_passwordFormatInvalid_shouldThrow_PasswordFormatException(){

        when(userRepo.existsByUsername(registerUserRequestDto.getUsername())).thenReturn(false);

        when(userRepo.existsByEmail(registerUserRequestDto.getEmail())).thenReturn(false);

        doThrow(new PasswordFormatException(PASSWORD_FORMAT_EXCEPTION_MESSAGE))
            .when(passwordValidator).validate(registerUserRequestDto.getPassword());

        PasswordFormatException ex = assertThrows(PasswordFormatException.class, ()->{
            authService.register(registerUserRequestDto);
        });

        assertEquals(PASSWORD_FORMAT_EXCEPTION_MESSAGE, ex.getMessage());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any());
    }

    @Test
    public void userRegister_successful() {

        String plainPassword = registerUserRequestDto.getPassword();
        String encondedPassword = "encoded";

        when(userRepo.existsByUsername(registerUserRequestDto.getUsername())).thenReturn(false);
        when(userRepo.existsByEmail(registerUserRequestDto.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(anyString())).thenReturn(encondedPassword);
        when(mapper.fromDto(registerUserRequestDto)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(user);

        User registeredUser = authService.register(registerUserRequestDto);

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
        authService.verifyUser(user);

        assertTrue(user.isVerified());
        verify(userRepo).save(user);
    }

    @Test
    void test_findUserByUsernameOrEmail_When_UsernameAndEmail_AreNull(){

        LoginRequestDto loginRequestDto = new LoginRequestDto(null, null, null);

        InvalidLoginCredentialsException thrownException = assertThrows(
            InvalidLoginCredentialsException.class, 
                ()->{
                    authService.login(loginRequestDto);
                }
            );

        assertEquals(NEEDED_USERNAME_OR_EMAIL, thrownException.getMessage());

        verify(userRepo, times(0)).findByUsernameOrEmail(anyString(), anyString());
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
        verify(authManager, times(0)).authenticate(any());
        verify(jwtService, times(0)).generateToken(anyString());
    }

    @Test
    void test_findUserByUsernameOrEmail_WhenPassing_UsernameAndEmail_ForNotExistentUser(){

        LoginRequestDto loginRequestDto = new LoginRequestDto("testUsername", "test@email", "testPassword");

        when(userRepo.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        UserNotFoundException thrownException = assertThrows(
            UserNotFoundException.class, 
                () -> {
                    authService.login(loginRequestDto);   
                }
            );

        assertEquals(USER_NOT_FOUND, thrownException.getMessage());

        verify(userRepo, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
        verify(authManager, times(0)).authenticate(any());
        verify(jwtService, times(0)).generateToken(anyString());

    }

    @Test
    void test_findUserByUsernameOrEmail_WhenPassing_OnlyUsername_ForNotExistentUser(){
        LoginRequestDto loginRequestDto = new LoginRequestDto("testUsername", null, "testPassword");

        when(userRepo.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        UserNotFoundException thrownException = assertThrows(
            UserNotFoundException.class, 
                () -> {
                    authService.login(loginRequestDto);   
                }
            );

        assertEquals(USER_NOT_FOUND, thrownException.getMessage());

        verify(userRepo, times(1)).findByUsernameOrEmail(loginRequestDto.getUsername(), null);
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
        verify(authManager, times(0)).authenticate(any());
        verify(jwtService, times(0)).generateToken(anyString());
    }

    @Test
    void test_findUserByUsernameOrEmail_WhenPassing_OnlyEmail_ForNotExistentUser(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(null, "test@email", "testPassword");

        when(userRepo.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        UserNotFoundException thrownException = assertThrows(
            UserNotFoundException.class, 
                () -> {
                    authService.login(loginRequestDto);   
                }
            );

        assertEquals(USER_NOT_FOUND, thrownException.getMessage());

        verify(userRepo, times(1)).findByUsernameOrEmail(null, loginRequestDto.getEmail());
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
        verify(authManager, times(0)).authenticate(any());
        verify(jwtService, times(0)).generateToken(anyString());
    }

    @Test
    void test_findUserByUsernameOrEmail_WhenPassing_UsernameAndEmail_ForRegisteredUser_InvalidPassword(){
        LoginRequestDto loginRequestDto = new LoginRequestDto("testUsername", "test@email", "testPassword");

        when(userRepo.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).thenReturn(false);

        InvalidLoginCredentialsException thrownException = assertThrows(
            InvalidLoginCredentialsException.class, 
                () -> {
                    authService.login(loginRequestDto);
                }
            );
        
        assertEquals(INVALID_PASSWORD, thrownException.getMessage());

        verify(userRepo, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(authManager, times(0)).authenticate(any());
        verify(jwtService, times(0)).generateToken(anyString());
    }

    @Test
    void test_findUserByUsernameOrEmail_WhenPassing_UsernameAndEmail_ForRegisteredUser_CorrectPassword(){
        LoginRequestDto loginRequestDto = new LoginRequestDto("testUsername", "test@email", "testPassword");

        when(userRepo.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).thenReturn(true);

        authService.login(loginRequestDto);

        verify(userRepo, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(authManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(anyString());
    }

    @Test
    void test_findUserByUsernameOrEmail_WhenPassing_OnlyUsername_ForRegisteredUser_CorrectPassword(){
        LoginRequestDto loginRequestDto = new LoginRequestDto("testUsername", null, "testPassword");

        when(userRepo.findByUsernameOrEmail(loginRequestDto.getUsername(), null)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).thenReturn(true);

        authService.login(loginRequestDto);

        verify(userRepo, times(1)).findByUsernameOrEmail(loginRequestDto.getUsername(), null);
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(authManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(anyString());
    }

    @Test
    void test_findUserByUsernameOrEmail_WhenPassing_OnlyEmail_ForRegisteredUser_CorrectPassword(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(null, "test@email", "testPassword");

        when(userRepo.findByUsernameOrEmail(null, loginRequestDto.getEmail())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).thenReturn(true);

        authService.login(loginRequestDto);

        verify(userRepo, times(1)).findByUsernameOrEmail(null, loginRequestDto.getEmail());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(authManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(anyString());
    }

}
