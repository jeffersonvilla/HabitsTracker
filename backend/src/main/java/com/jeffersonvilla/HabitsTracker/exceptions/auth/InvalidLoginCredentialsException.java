package com.jeffersonvilla.HabitsTracker.exceptions.auth;

public class InvalidLoginCredentialsException extends RuntimeException{

    public InvalidLoginCredentialsException(String message){
        super(message);
    }

    public InvalidLoginCredentialsException(String message, Throwable cause){
        super(message, cause);
    }
    
}
