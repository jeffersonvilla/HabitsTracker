package com.jeffersonvilla.HabitsTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeffersonvilla.HabitsTracker.model.HabitCategory;

@Repository
public interface HabitCategoryRepo extends JpaRepository<HabitCategory, Long>{
    
}
