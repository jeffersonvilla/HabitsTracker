package com.jeffersonvilla.HabitsTracker.exceptions.habit;

public class HabitCategoryAccessDeniedException extends RuntimeException{
    
    public HabitCategoryAccessDeniedException(String message){
        super(message);
    }

    public HabitCategoryAccessDeniedException(String message, Throwable cause){
        super(message, cause);
    }
}
