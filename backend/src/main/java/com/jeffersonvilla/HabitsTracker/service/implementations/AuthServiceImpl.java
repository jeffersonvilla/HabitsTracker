package com.jeffersonvilla.HabitsTracker.service.implementations;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jeffersonvilla.HabitsTracker.Dto.RegisterUserRequestDto;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.EmailInUseException;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.InvalidLoginCredentialsException;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.UsernameInUseException;
import com.jeffersonvilla.HabitsTracker.Dto.LoginRequestDto;
import com.jeffersonvilla.HabitsTracker.mapper.Mapper;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;
import com.jeffersonvilla.HabitsTracker.service.interfaces.AuthService;
import com.jeffersonvilla.HabitsTracker.util.JwtService;
import com.jeffersonvilla.HabitsTracker.validation.Validator;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.*;

import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepo userRepo;
    private final Mapper<User, RegisterUserRequestDto> mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Validator<String> passwordValidator;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepo userRepo
        , Mapper<User, RegisterUserRequestDto> mapper
        , BCryptPasswordEncoder passwordEncoder
        , Validator<String> passwordValidator
        , AuthenticationManager authManager
        , JwtService jwtService){

        this.userRepo = userRepo;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Override
    public User register(RegisterUserRequestDto userDto) { 

        if(userRepo.existsByUsername(userDto.getUsername())) 
            throw new UsernameInUseException(USERNAME_IN_USE_MESSAGE);
    
        if(userRepo.existsByEmail(userDto.getEmail())) 
            throw new EmailInUseException(EMAIL_IN_USE_MESSAGE);
        
        passwordValidator.validate(userDto.getPassword());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepo.save(mapper.fromDto(userDto));
    }

    @Override
    public void verifyUser(User user) {
        
        user.setVerified(true);
        userRepo.save(user);
    }

    @Override
    public String login(LoginRequestDto loginDto) {
        
        User userFound = findUserByUsernameOrEmail(loginDto);

        if(!passwordEncoder.matches(loginDto.getPassword(), userFound.getPassword())){
            throw new InvalidLoginCredentialsException(INVALID_PASSWORD);
        }

        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                (loginDto.getEmail() != null)? loginDto.getEmail() : loginDto.getUsername()
                , loginDto.getPassword()
            )
        );

        return jwtService.generateToken(userFound.getUsername(), userFound.getId());

    }

    private User findUserByUsernameOrEmail(LoginRequestDto loginDto){

        //In case the request for login doesn't have neither the username or email
        if(loginDto.getUsername() == null && loginDto.getEmail() == null){
            throw new InvalidLoginCredentialsException(NEEDED_USERNAME_OR_EMAIL);
        }

        Optional<User> user = userRepo.findByUsernameOrEmail(loginDto.getUsername(), loginDto.getEmail());        
        
        //In case there is no user with the username or email
        if(user.isEmpty()){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        return user.get();
    }
    
}
