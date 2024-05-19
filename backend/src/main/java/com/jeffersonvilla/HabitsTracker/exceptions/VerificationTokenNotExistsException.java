package com.jeffersonvilla.HabitsTracker.exceptions;

public class VerificationTokenNotExistsException extends RuntimeException {

    public VerificationTokenNotExistsException(String message){
        super(message);
    }

    public VerificationTokenNotExistsException(String message, Throwable cause){
        super(message, cause);
    }
    
}
