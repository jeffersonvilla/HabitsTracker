package com.jeffersonvilla.HabitsTracker.controller;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.HABIT_DELETED_SUCCESS;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitDto;
import com.jeffersonvilla.HabitsTracker.service.interfaces.HabitService;

import jakarta.validation.Valid;

/**
 * Error Responses are on the GlobalExceptionHandler
 */

@RestController
@RequestMapping("/api/v1/habit")
@CrossOrigin(origins = "http://localhost:3000")
public class HabitController {
    
    private final HabitService habitService;

    public HabitController(HabitService habitService){
        this.habitService = habitService;
    }

    @PostMapping("/")
    public ResponseEntity<HabitDto> createHabit(@RequestBody @Valid HabitDto habit){

        HabitDto createdHabit = habitService.createHabit(habit);

        return new ResponseEntity<HabitDto>(createdHabit, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitDto>> listAllHabits(@PathVariable Long userId){

        List<HabitDto> habits = habitService.getAllHabits(userId);

        return new ResponseEntity<List<HabitDto>>(habits, HttpStatus.OK);
    }

    @GetMapping("/{habitId}")
    public ResponseEntity<HabitDto> getHabit(@PathVariable Long habitId){

        HabitDto habit = habitService.getHabit(habitId);

        return new ResponseEntity<HabitDto>(habit, HttpStatus.OK);
    }

    @PutMapping("/{habitId}")
    public ResponseEntity<HabitDto> updateHabit(@PathVariable Long habitId, 
        @RequestBody HabitDto habitDto){

        HabitDto habit = habitService.updateHabit(habitId, habitDto);

        return new ResponseEntity<HabitDto>(habit, HttpStatus.OK);
    }

    @DeleteMapping("/{habitId}")
    public ResponseEntity<String> deleteHabit(@PathVariable Long habitId){

    habitService.deleteHabit(habitId);

    return new ResponseEntity<String>(HABIT_DELETED_SUCCESS, HttpStatus.OK);
}
}
