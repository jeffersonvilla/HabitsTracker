package com.jeffersonvilla.HabitsTracker.service.interfaces;

import com.jeffersonvilla.HabitsTracker.Dto.RegisterUserRequestDto;
import com.jeffersonvilla.HabitsTracker.Dto.LoginRequestDto;
import com.jeffersonvilla.HabitsTracker.model.User;

public interface AuthService {
    
    public User register(RegisterUserRequestDto userDto);

    public void verifyUser(User user);

    public String login(LoginRequestDto loginDto);
    
}
