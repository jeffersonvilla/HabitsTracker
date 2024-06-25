package com.jeffersonvilla.HabitsTracker.mapper;

import org.springframework.stereotype.Component;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitCategoryDto;
import com.jeffersonvilla.HabitsTracker.model.HabitCategory;

@Component
public class HabitCategoryMapper implements Mapper<HabitCategory, HabitCategoryDto>{

    @Override
    public HabitCategory fromDto(HabitCategoryDto dto) {
        return new HabitCategory(dto.getCategoryId(), dto.getName(), null);
    }

    @Override
    public HabitCategoryDto toDto(HabitCategory model) {
        return new HabitCategoryDto(model.getId(),model.getName());
    }
    
    
}
