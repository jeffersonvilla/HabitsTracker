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

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitDto;
import com.jeffersonvilla.HabitsTracker.service.interfaces.HabitService;

/**
 * Error cases are on the GlobalExcpetionHandler
 * */
public class HabitControllerTests {

    @Mock
    private HabitService habitService;

    @InjectMocks
    private HabitController habitController;

    private HabitDto dtoRequest;

    private HabitDto dtoResponse;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        dtoRequest = new HabitDto(null, "Wake up early", "Wake up before 9:00 a.m.", "Alarm", 1L, 1L);
        dtoResponse = new HabitDto(1L, "Wake up early", "Wake up before 9:00 a.m.", "Alarm", 1L, 1L);
    }

    @Test
    public void test_createHabit_sucess(){

        when(habitService.createHabit(dtoRequest)).thenReturn(dtoResponse);

        ResponseEntity<HabitDto> response = habitController.createHabit(dtoRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(habitService).createHabit(any());
    }

}
