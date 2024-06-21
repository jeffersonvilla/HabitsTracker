package com.jeffersonvilla.HabitsTracker.exceptions.auth;

public class VerificationTokenNotExistsException extends RuntimeException {

    public VerificationTokenNotExistsException(String message){
        super(message);
    }

    public VerificationTokenNotExistsException(String message, Throwable cause){
        super(message, cause);
    }
    
}
