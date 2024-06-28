package com.jeffersonvilla.HabitsTracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitCategoryDto;
import com.jeffersonvilla.HabitsTracker.service.interfaces.HabitCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "List all habit categories of a user", 
        description = "Returns a list of all habit categories for a given user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitCategoryDto>> listAllHabitCategories(
            @Parameter(description = "ID of the user to fetch habit categories for") 
            @PathVariable Long userId) {

        List<HabitCategoryDto> categories = habitCategoryService.getAllHabitCategories(userId);
        return new ResponseEntity<List<HabitCategoryDto>>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Get habit category by Id", 
        description = "Returns the habit category for a given ID")
    @GetMapping("/{categoryId}")
    public ResponseEntity<HabitCategoryDto> getHabitCategory(
            @Parameter(description = "ID of the habit category to fetch") 
            @PathVariable Long categoryId) {

        HabitCategoryDto category = habitCategoryService.getHabitCategory(categoryId);
        return new ResponseEntity<HabitCategoryDto>(category, HttpStatus.OK);
    }

    @Operation(summary = "Update habit category by Id", 
        description = "Returns the habit category updated with the data of the request body")
    @PutMapping("/{categoryId}")
    public ResponseEntity<HabitCategoryDto> updateHabitCategory(
            @Parameter(description = "ID of the habit category to update") 
            @PathVariable Long categoryId, @RequestBody HabitCategoryDto dto) {

        HabitCategoryDto category = habitCategoryService.updateHabitCategory(categoryId, dto);
        return new ResponseEntity<HabitCategoryDto>(category, HttpStatus.OK);
    }

    @Operation(summary = "Delete habit category of a user", 
        description = "Delete the habit category for a given ID")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteHabitCategory(
            @Parameter(description = "ID of habit category to delete") 
            @PathVariable Long categoryId) {
    
        habitCategoryService.deleteHabitCategory(categoryId);

        return new ResponseEntity<String>("Habit category deleted successfully", HttpStatus.OK);
    }
}
