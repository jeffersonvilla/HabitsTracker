package com.jeffersonvilla.HabitsTracker.service.interfaces;

import java.util.List;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitCategoryDto;

public interface HabitCategoryService {
    
    public HabitCategoryDto createHabitCategory(HabitCategoryDto dto);

    public List<HabitCategoryDto> getAllHabitCategories(Long userId);

    public HabitCategoryDto getHabitCategory(Long categoryId);

    public HabitCategoryDto updateHabitCategory(Long categoryId, HabitCategoryDto dto);

    public void deleteHabitCategory(Long categoryId);

}
