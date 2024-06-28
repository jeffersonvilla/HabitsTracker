package com.jeffersonvilla.HabitsTracker.exceptions.habit;

public class HabitCategoryDeniedDeleteException extends RuntimeException{
    
    public HabitCategoryDeniedDeleteException(String message){
        super(message);
    }

    public HabitCategoryDeniedDeleteException(String message, Throwable cause){
        super(message, cause);
    }
}
