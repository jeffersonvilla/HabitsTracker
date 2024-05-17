package com.jeffersonvilla.HabitsTracker.service.implementations;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jeffersonvilla.HabitsTracker.Dto.UserDto;
import com.jeffersonvilla.HabitsTracker.exceptions.EmailInUseException;
import com.jeffersonvilla.HabitsTracker.exceptions.UsernameInUseException;
import com.jeffersonvilla.HabitsTracker.mapper.Mapper;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;
import com.jeffersonvilla.HabitsTracker.service.interfaces.UserService;
import com.jeffersonvilla.HabitsTracker.validation.Validator;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final Mapper<User, UserDto> mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Validator<String> passwordValidator;

    private final String USERNAME_IN_USE_MESSAGE = "Username already exists.";
    private final String EMAIL_IN_USE_MESSAGE = "Email already exists.";

    public UserServiceImpl(UserRepo userRepo, Mapper<User, UserDto> mapper
        , BCryptPasswordEncoder passwordEncoder
        , Validator<String> passwordValidator){

        this.userRepo = userRepo;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public User register(UserDto userDto) { 

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
    
}
