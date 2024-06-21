package com.jeffersonvilla.HabitsTracker.service;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.HABIT_CATEGORY_NOT_FOUND;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_AUTORIZED_ACCESS_HABITS_FOR_USER;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_AUTORIZED_TO_CREATE_HABIT_FOR_USER;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitDto;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitAccessDeniedException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCreationDeniedException;
import com.jeffersonvilla.HabitsTracker.mapper.Mapper;
import com.jeffersonvilla.HabitsTracker.model.Habit;
import com.jeffersonvilla.HabitsTracker.model.HabitCategory;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.repository.HabitCategoryRepo;
import com.jeffersonvilla.HabitsTracker.repository.HabitRepo;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;
import com.jeffersonvilla.HabitsTracker.service.implementations.HabitServiceImpl;

public class HabitServiceImplTests {
    
    @Mock
    private HabitRepo habitRepo;

    @Mock
    private HabitCategoryRepo habitCategoryRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private Mapper<Habit, HabitDto> mapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private HabitServiceImpl habitService;

    private HabitDto dto;

    private User user;

    private HabitCategory category;

    private Habit habit;

    private final String USERNAME = "usernameTest";

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        dto = new HabitDto(1L, "Wake up early", "Wake up before 9:00 a.m.", "Alarm", 1L, 1L);
        user = new User(1L, USERNAME, "test@email", "password", true);
        category = new HabitCategory(1L, "Health", null);
        habit = new Habit(1L, "Wake up early", "Wake up before 9:00 a.m.", "Alarm", category, user);
    }

    /**
     * When the user id passed on the dto does not exist on database
     * */ 
    @Test
    public void createHabit_userNotExists(){

        when(userRepo.findById(dto.getUser())).thenReturn(Optional.empty());

        UserNotFoundException exceptionThrown = assertThrows(
            UserNotFoundException.class,
            () -> {
                habitService.createHabit(dto);
            });

        assertEquals(USER_NOT_FOUND, exceptionThrown.getMessage());

        verify(userRepo).findById(anyLong());
        verify(securityContext, times(0)).getAuthentication();
        verify(habitCategoryRepo, times(0)).findById(anyLong());
        verify(habitRepo, times(0)).save(any());
        verify(mapper, times(0)).toDto(any());

    }

    /**
     * When the username signed on the jwt is not the same of the one found in
     * the database (with the id passed on the dto)
     * */
    @Test
    public void createHabit_userNotAuthorized(){

        when(userRepo.findById(dto.getUser())).thenReturn(Optional.of(user));

        when(authentication.getName()).thenReturn("otherUsername");

        HabitCreationDeniedException exceptionThrown = assertThrows(
            HabitCreationDeniedException.class,
            () -> {
                habitService.createHabit(dto);
            });

        assertEquals(USER_NOT_AUTORIZED_TO_CREATE_HABIT_FOR_USER, exceptionThrown.getMessage());

        verify(userRepo).findById(anyLong());
        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(habitCategoryRepo, times(0)).findById(anyLong());
        verify(habitRepo, times(0)).save(any());
        verify(mapper, times(0)).toDto(any());

    }

    @Test
    public void createHabit_habitCategory_DoesNotExist(){

        when(userRepo.findById(dto.getUser())).thenReturn(Optional.of(user));

        when(authentication.getName()).thenReturn(USERNAME);

        when(habitCategoryRepo.findById(dto.getCategory())).thenReturn(Optional.empty());

        HabitCategoryNotFoundException exceptionThrown = assertThrows(
            HabitCategoryNotFoundException.class,
            () -> {
                habitService.createHabit(dto);
            });

        assertEquals(HABIT_CATEGORY_NOT_FOUND, exceptionThrown.getMessage());

        verify(userRepo).findById(anyLong());
        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(habitCategoryRepo).findById(anyLong());
        verify(habitRepo, times(0)).save(any());
        verify(mapper, times(0)).toDto(any());

    }

    @Test
    public void createHabit_Successful(){
        when(userRepo.findById(dto.getUser())).thenReturn(Optional.of(user));

        when(authentication.getName()).thenReturn(USERNAME);

        when(habitCategoryRepo.findById(dto.getCategory())).thenReturn(Optional.of(category));

        when(habitRepo.save(any())).thenReturn(habit);

        when(mapper.toDto(any())).thenReturn(dto);

        HabitDto response = habitService.createHabit(dto);

        assertEquals(dto.toString(), response.toString());

        verify(userRepo).findById(anyLong());
        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(habitCategoryRepo).findById(anyLong());
        verify(habitRepo).save(any());
        verify(mapper).toDto(any());
    }

    /**
     * When the user id passed as argument does not exist on database
     * */ 
    @Test
    public void getAllHabits_user_NotFound(){

        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exceptionThrown = assertThrows(
            UserNotFoundException.class, 
            () -> {
                habitService.getAllHabits(1L);
            }
        );

        assertEquals(USER_NOT_FOUND, exceptionThrown.getMessage());

        verify(userRepo).findById(anyLong());
        verify(securityContext, times(0)).getAuthentication();
        verify(habitRepo, times(0)).findByUser(any());
        verify(mapper, times(0)).toDto(any());

    }

    /**
     * When the username signed on the jwt is not the same of the one found in
     * the database (with the id passed as argument)
     * */
    @Test
    public void getAllHabits_userNotAuthorized(){

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        when(authentication.getName()).thenReturn("otherUsername");

        HabitAccessDeniedException exceptionThrown = assertThrows(
            HabitAccessDeniedException.class,
            () -> {
                habitService.getAllHabits(user.getId());
            });

        assertEquals(USER_NOT_AUTORIZED_ACCESS_HABITS_FOR_USER, exceptionThrown.getMessage());

        verify(userRepo).findById(anyLong());
        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(habitRepo, times(0)).findByUser(any());
        verify(mapper, times(0)).toDto(any());

    }

    @Test
    public void getAllHabits_success(){

        HabitDto habitDto1 = mock(HabitDto.class);
        HabitDto habitDto2 = mock(HabitDto.class);
        List<HabitDto> expectedList = List.of(habitDto1, habitDto2);

        Habit habit1 = mock(Habit.class);
        Habit habit2 = mock(Habit.class);
        List<Habit> habitsFound = List.of(habit1, habit2);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        when(authentication.getName()).thenReturn(USERNAME);

        when(habitRepo.findByUser(any())).thenReturn(habitsFound);
        
        when(mapper.toDto(habit1)).thenReturn(habitDto1);
        when(mapper.toDto(habit2)).thenReturn(habitDto2);

        List<HabitDto> resultList = habitService.getAllHabits(user.getId());
        
        assertEquals(expectedList, resultList);

        verify(userRepo).findById(anyLong());
        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(habitRepo).findByUser(any());
        verify(mapper, times(habitsFound.size())).toDto(any());

    }
}
