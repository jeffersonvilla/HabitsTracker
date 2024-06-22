package com.jeffersonvilla.HabitsTracker.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

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
    public void test_createHabit_success(){

        when(habitService.createHabit(dtoRequest)).thenReturn(dtoResponse);

        ResponseEntity<HabitDto> response = habitController.createHabit(dtoRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(habitService).createHabit(any());
    }

    @Test
    public void test_listAllHabits_success(){

        HabitDto habit1 = mock(HabitDto.class);
        HabitDto habit2 = mock(HabitDto.class);
        List<HabitDto> resultList = List.of(habit1, habit2);

        when(habitService.getAllHabits(anyLong())).thenReturn(resultList);

        ResponseEntity<List<HabitDto>> response = habitController.listAllHabits(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(resultList, response.getBody());

        verify(habitService).getAllHabits(anyLong());
    }

    @Test
    public void test_getHabit_success(){

        HabitDto habitDto = mock(HabitDto.class);

        when(habitService.getHabit(anyLong())).thenReturn(habitDto);

        ResponseEntity<HabitDto> reponse = habitController.getHabit(1L);

        assertEquals(HttpStatus.OK, reponse.getStatusCode());
        assertNotNull(reponse.getBody());
        assertEquals(habitDto, reponse.getBody());

        verify(habitService).getHabit(anyLong());
    }

}
