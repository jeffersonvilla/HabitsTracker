package com.jeffersonvilla.HabitsTracker.exceptions.auth;

public class EmailInUseException extends RuntimeException{
    
    public EmailInUseException(String message){
        super(message);
    }

    public EmailInUseException(String message, Throwable cause){
        super(message, cause);
    }
}
