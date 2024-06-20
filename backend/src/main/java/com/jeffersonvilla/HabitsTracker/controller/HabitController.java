package com.jeffersonvilla.HabitsTracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitDto;
import com.jeffersonvilla.HabitsTracker.service.interfaces.HabitService;

import jakarta.validation.Valid;

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
}
