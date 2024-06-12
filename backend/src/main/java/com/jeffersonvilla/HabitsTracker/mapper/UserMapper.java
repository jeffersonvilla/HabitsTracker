package com.jeffersonvilla.HabitsTracker.mapper;

import org.springframework.stereotype.Component;

import com.jeffersonvilla.HabitsTracker.Dto.RegisterUserRequestDto;
import com.jeffersonvilla.HabitsTracker.model.User;

@Component
public class UserMapper implements Mapper<User, RegisterUserRequestDto>{

    @Override
    public RegisterUserRequestDto toDto(User model) {
        RegisterUserRequestDto dto = new RegisterUserRequestDto();
        dto.setUsername(model.getUsername());
        dto.setEmail(model.getEmail());
        //dto.setPassword(model.getPassword());
        return dto;
    }

    @Override
    public User fromDto(RegisterUserRequestDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
    
}
