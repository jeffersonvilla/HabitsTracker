package com.jeffersonvilla.HabitsTracker.exceptions.habit;

public class HabitCategoryNotFoundException extends RuntimeException{

    public HabitCategoryNotFoundException(String message){
        super(message);
    }

    public HabitCategoryNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
    
}
