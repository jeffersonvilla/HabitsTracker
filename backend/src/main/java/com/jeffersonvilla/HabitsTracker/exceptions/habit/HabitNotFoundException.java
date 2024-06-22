package com.jeffersonvilla.HabitsTracker.exceptions.habit;

public class HabitNotFoundException extends RuntimeException {
    
    public HabitNotFoundException(String message){
        super(message);
    }

    public HabitNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
