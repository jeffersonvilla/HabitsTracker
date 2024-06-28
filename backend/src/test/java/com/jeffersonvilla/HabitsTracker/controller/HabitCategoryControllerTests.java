package com.jeffersonvilla.HabitsTracker.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void test_listAllHabitCategories(){

        List<HabitCategoryDto> responseList = new ArrayList<>();

        responseList.add(responseDto);
        responseList.add(new HabitCategoryDto(2L, "testCategory2"));

        when(habitCategoryService.getAllHabitCategories(anyLong())).thenReturn(responseList);

        ResponseEntity<List<HabitCategoryDto>> response = 
            habitCategoryController.listAllHabitCategories(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(habitCategoryService).getAllHabitCategories(anyLong());
    }

    @Test
    public void test_getHabitCategory(){
        
        when(habitCategoryService.getHabitCategory(anyLong())).thenReturn(responseDto);

        ResponseEntity<HabitCategoryDto> response = 
            habitCategoryController.getHabitCategory(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(habitCategoryService).getHabitCategory(anyLong());
    }

    @Test
    public void test_updateHabitCategory(){
        
        when(habitCategoryService.updateHabitCategory(anyLong(), any())).thenReturn(responseDto);

        ResponseEntity<HabitCategoryDto> response = 
            habitCategoryController.updateHabitCategory(1L, requestDto);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(habitCategoryService).updateHabitCategory(anyLong(),any());
    }

    @Test
    public void test_deleteHabitCategory(){

        ResponseEntity<String> response = habitCategoryController.deleteHabitCategory(11L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(habitCategoryService).deleteHabitCategory(anyLong());
    }
}
