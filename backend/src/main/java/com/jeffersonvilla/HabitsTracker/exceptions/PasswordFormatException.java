package com.jeffersonvilla.HabitsTracker.exceptions;

public class PasswordFormatException extends RuntimeException{

    public PasswordFormatException(String message){
        super(message);
    }

    public PasswordFormatException(String message, Throwable cause){
        super(message, cause);
    }
    
}
