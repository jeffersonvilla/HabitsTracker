package com.jeffersonvilla.HabitsTracker.exceptions.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.jeffersonvilla.HabitsTracker.exceptions.auth.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitAccessDeniedException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryAccessDeniedException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryDeniedDeleteException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCreationDeniedException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitNotFoundException;

public class GlobalExceptionHandlerTests {
    
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_handleValidationException(){
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = mock(FieldError.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldError()).thenReturn(fieldError);

        ResponseEntity<ErrorResponse> errorResponse = globalExceptionHandler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.toString(), errorResponse.getBody().getStatus());
        assertNotNull(errorResponse.getBody().getMessage());
    }

    @Test
    void test_handleBadRequest_Register_Login(){
        String message = "message";

        RuntimeException ex = new RuntimeException(message);

        ResponseEntity<ErrorResponse> errorResponse = globalExceptionHandler.handleBadRequest_Register_Login(ex);

        assertEquals(HttpStatus.BAD_REQUEST.toString(), errorResponse.getBody().getStatus());
        assertEquals(message, errorResponse.getBody().getMessage());
    }

    @Test
    void test_handleUserNotFound(){
        
        String message = "message";

        UserNotFoundException ex = new UserNotFoundException(message);

        ResponseEntity<ErrorResponse> errorResponse = globalExceptionHandler.handleUserNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND.toString(), errorResponse.getBody().getStatus());
        assertEquals(message, errorResponse.getBody().getMessage());
    }

    @Test
    void test_handleHabitCategoryNotFound(){
        
        String message = "message";

        HabitCategoryNotFoundException ex = new HabitCategoryNotFoundException(message);

        ResponseEntity<ErrorResponse> errorResponse = globalExceptionHandler.handleHabitCategoryNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND.toString(), errorResponse.getBody().getStatus());
        assertEquals(message, errorResponse.getBody().getMessage());
    }

    @Test
    void test_handleHabit_Creation_Denied(){
        
        String message = "message";

        HabitCreationDeniedException ex = new HabitCreationDeniedException(message);

        ResponseEntity<ErrorResponse> errorResponse = globalExceptionHandler.handleHabitCreationDenied(ex);

        assertEquals(HttpStatus.UNAUTHORIZED.toString(), errorResponse.getBody().getStatus());
        assertEquals(message, errorResponse.getBody().getMessage());
    }

    @Test
    void test_handleHabit_Access_Denied(){
        
        String message = "message";

        HabitAccessDeniedException ex = new HabitAccessDeniedException(message);

        ResponseEntity<ErrorResponse> errorResponse = globalExceptionHandler.handleHabitAccessDenied(ex);

        assertEquals(HttpStatus.UNAUTHORIZED.toString(), errorResponse.getBody().getStatus());
        assertEquals(message, errorResponse.getBody().getMessage());
    }

    @Test
    public void test_ErrorReponse_ConstructorAndGetters() {

        ErrorResponse errorResponse = new ErrorResponse("400", "Bad Request");

        assertEquals("400", errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getMessage());
    }

    @Test
    public void test_ErrorReponse_Setters() {

        ErrorResponse errorResponse = new ErrorResponse("500", "Internal Server Error");

        errorResponse.setStatus("503");
        errorResponse.setMessage("Service Unavailable");

        assertEquals("503", errorResponse.getStatus());
        assertEquals("Service Unavailable", errorResponse.getMessage());
    }

    @Test
    void test_handleHabitNotFound(){
        
        String message = "message";

        HabitNotFoundException ex = new HabitNotFoundException(message);

        ResponseEntity<ErrorResponse> errorResponse = globalExceptionHandler.handleHabitNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND.toString(), errorResponse.getBody().getStatus());
        assertEquals(message, errorResponse.getBody().getMessage());
    }

    @Test
    void test_handleHabitCategoryAccessDeniedException(){
        
        String message = "message";

        HabitCategoryAccessDeniedException ex = new HabitCategoryAccessDeniedException(message);

        ResponseEntity<ErrorResponse> errorResponse = 
            globalExceptionHandler.handleHabitCategoryAccessDeniedException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED.toString(), errorResponse.getBody().getStatus());
        assertEquals(message, errorResponse.getBody().getMessage());
    }

    

    @Test
    void test_handleHabitCategoryDeniedDeleteException(){

        String message = "message";

        HabitCategoryDeniedDeleteException ex = new HabitCategoryDeniedDeleteException(message);

        ResponseEntity<ErrorResponse> errorResponse = 
            globalExceptionHandler.handleHabitCategoryDeniedDeleteException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.toString(), errorResponse.getBody().getStatus());
        assertEquals(message, errorResponse.getBody().getMessage());
    }
}   
