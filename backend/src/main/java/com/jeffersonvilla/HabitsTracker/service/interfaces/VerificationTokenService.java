package com.jeffersonvilla.HabitsTracker.service.interfaces;

import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.model.VerificationToken;

public interface VerificationTokenService {
    
    public VerificationToken generateToken(User user);
    
    public VerificationToken verifyToken(String token);
}
