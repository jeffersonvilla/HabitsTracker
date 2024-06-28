package com.jeffersonvilla.HabitsTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jeffersonvilla.HabitsTracker.model.HabitCategory;

@Repository
public interface HabitCategoryRepo extends JpaRepository<HabitCategory, Long>{
    
    @Query("SELECT h FROM HabitCategory h WHERE h.user IS NULL OR h.user.id = :userId")
    public List<HabitCategory> findHabitCategoriesByUser(@Param("userId") Long userId);

}
