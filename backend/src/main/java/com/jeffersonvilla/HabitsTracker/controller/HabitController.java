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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;

/**
 * Error Responses are on the GlobalExceptionHandler
 */
@RestController
@RequestMapping("/api/v1/habit")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Habit", description = "The Habit API")
@Validated
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @Operation(summary = "Create a new habit", description = "Creates a new habit and returns the created habit")
    @PostMapping("/")
    public ResponseEntity<HabitDto> createHabit(@RequestBody @Valid HabitDto habit) {
        HabitDto createdHabit = habitService.createHabit(habit);
        return new ResponseEntity<>(createdHabit, HttpStatus.CREATED);
    }

    @Operation(summary = "List all habits of a user", description = "Returns a list of all habits for a given user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitDto>> listAllHabits(
            @Parameter(description = "ID of the user to fetch habits for") @PathVariable Long userId) {
        List<HabitDto> habits = habitService.getAllHabits(userId);
        return new ResponseEntity<>(habits, HttpStatus.OK);
    }

    @Operation(summary = "Get a habit by ID", description = "Returns a habit by its ID")
    @GetMapping("/{habitId}")
    public ResponseEntity<HabitDto> getHabit(
            @Parameter(description = "ID of the habit to fetch") @PathVariable Long habitId) {
        HabitDto habit = habitService.getHabit(habitId);
        return new ResponseEntity<>(habit, HttpStatus.OK);
    }

    @Operation(summary = "Update a habit by ID", description = "Updates a habit and returns the updated habit")
    @PutMapping("/{habitId}")
    public ResponseEntity<HabitDto> updateHabit(
            @Parameter(description = "ID of the habit to update") @PathVariable Long habitId,
            @RequestBody HabitDto habitDto) {
        HabitDto habit = habitService.updateHabit(habitId, habitDto);
        return new ResponseEntity<>(habit, HttpStatus.OK);
    }

    @Operation(summary = "Delete a habit by ID", description = "Deletes a habit and returns a success message")
    @DeleteMapping("/{habitId}")
    public ResponseEntity<String> deleteHabit(
            @Parameter(description = "ID of the habit to delete") @PathVariable Long habitId) {
        habitService.deleteHabit(habitId);
        return new ResponseEntity<>(HABIT_DELETED_SUCCESS, HttpStatus.OK);
    }
}
