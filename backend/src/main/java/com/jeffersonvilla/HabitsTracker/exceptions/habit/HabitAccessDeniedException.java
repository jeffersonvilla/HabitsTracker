package com.jeffersonvilla.HabitsTracker.exceptions.habit;

public class HabitAccessDeniedException extends RuntimeException{

    public HabitAccessDeniedException(String message){
        super(message);
    }
    
    public HabitAccessDeniedException(String message, Throwable cause){
        super(message, cause);
    }

}
