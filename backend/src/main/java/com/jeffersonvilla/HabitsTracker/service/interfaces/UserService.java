package com.jeffersonvilla.HabitsTracker.service.interfaces;

import com.jeffersonvilla.HabitsTracker.Dto.UserDto;
import com.jeffersonvilla.HabitsTracker.model.User;

public interface UserService {
    
    public User register(UserDto userDto);

    public void verifyUser(User user);
    
}
