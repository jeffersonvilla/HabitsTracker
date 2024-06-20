package com.jeffersonvilla.HabitsTracker.mapper;

import org.springframework.stereotype.Component;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitDto;
import com.jeffersonvilla.HabitsTracker.model.Habit;

@Component
public class HabitMapper implements Mapper<Habit, HabitDto>{


    @Override
    public HabitDto toDto(Habit model) {
        return new HabitDto(model.getId(), 
            model.getName(), 
            model.getDescription(), 
            model.getTrigger(),
            model.getCategory().getId(), 
            model.getUser().getId()
        );
    }

    @Override
    public Habit fromDto(HabitDto dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromDto'");
    }
    
}
