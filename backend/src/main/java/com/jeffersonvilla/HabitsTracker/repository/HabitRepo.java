package com.jeffersonvilla.HabitsTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeffersonvilla.HabitsTracker.model.Habit;
import com.jeffersonvilla.HabitsTracker.model.User;

import java.util.List;


@Repository
public interface HabitRepo extends JpaRepository<Habit, Long>{
    
    public List<Habit> findByUser(User user);
}
