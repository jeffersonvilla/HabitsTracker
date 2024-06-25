package com.jeffersonvilla.HabitsTracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitCategoryDto;
import com.jeffersonvilla.HabitsTracker.service.interfaces.HabitCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/habit-category")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Habit Category", description = "The Habit Category API")
@Validated
public class HabitCategoryController {
    
    private final HabitCategoryService habitCategoryService;

    public HabitCategoryController(HabitCategoryService habitCategoryService){
        this.habitCategoryService = habitCategoryService;
    }

    @Operation(summary = "Create a new habit category", 
        description = "Creates a new habit category and returns the created habit category")
    @PostMapping("/")
    public ResponseEntity<HabitCategoryDto> createHabitCategory(
        @RequestBody @Valid HabitCategoryDto dto){

        return new ResponseEntity<HabitCategoryDto>(
                habitCategoryService.createHabitCategory(dto),
                HttpStatus.CREATED);
    }
}
