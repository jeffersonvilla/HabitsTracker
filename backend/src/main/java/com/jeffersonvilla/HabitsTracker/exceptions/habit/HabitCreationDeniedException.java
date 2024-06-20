package com.jeffersonvilla.HabitsTracker.exceptions.habit;

public class HabitCreationDeniedException extends RuntimeException{

    public HabitCreationDeniedException(String message){
        super(message);
    }

    public HabitCreationDeniedException(String message, Throwable cause){
        super(message, cause);
    }
    
}
