package com.jeffersonvilla.HabitsTracker.service.interfaces;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitDto;

import java.util.List;

public interface HabitService {

    public HabitDto createHabit(HabitDto habitDto);

    public List<HabitDto> getAllHabits(long user);

    public HabitDto getHabit(long habitId);

    public HabitDto updateHabit(long habitId, HabitDto habitDto);

    public void deleteHabit(long id);
}
