package com.jeffersonvilla.HabitsTracker.config;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_FOUND;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jeffersonvilla.HabitsTracker.exceptions.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsernameOrEmail(username, username)
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }
    
}
