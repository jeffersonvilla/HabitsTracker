package com.jeffersonvilla.HabitsTracker.service.interfaces;

import com.jeffersonvilla.HabitsTracker.model.VerificationToken;

public interface EmailService {
    
    public void sendVerificationEmail(VerificationToken verficationToken);
}
