package com.jeffersonvilla.HabitsTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeffersonvilla.HabitsTracker.model.Habit;

@Repository
public interface HabitRepo extends JpaRepository<Habit, Long>{
    
}
