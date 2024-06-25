package com.jeffersonvilla.HabitsTracker.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitCategoryDto;
import com.jeffersonvilla.HabitsTracker.service.interfaces.HabitCategoryService;

public class HabitCategoryControllerTests {
    
    @Mock
    private HabitCategoryService habitCategoryService;

    @InjectMocks
    private HabitCategoryController habitCategoryController;

    private HabitCategoryDto requestDto;

    private HabitCategoryDto responseDto;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        requestDto = new HabitCategoryDto("testCategory");
        responseDto = new HabitCategoryDto(1L, "testCategory");
    }

    @Test
    public void test_createHabitCategory(){

        when(habitCategoryService.createHabitCategory(requestDto)).thenReturn(responseDto);

        ResponseEntity<HabitCategoryDto> response = habitCategoryController.createHabitCategory(requestDto);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(habitCategoryService).createHabitCategory(any());
    }
}
