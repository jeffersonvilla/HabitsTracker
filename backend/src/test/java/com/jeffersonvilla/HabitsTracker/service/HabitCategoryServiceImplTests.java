package com.jeffersonvilla.HabitsTracker.service;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.NOT_AUTHORIZED_TO_ACCESS_HABIT_CATEGORIES_OF_USER;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitCategoryDto;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryAccessDeniedException;
import com.jeffersonvilla.HabitsTracker.mapper.HabitCategoryMapper;
import com.jeffersonvilla.HabitsTracker.model.HabitCategory;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.repository.HabitCategoryRepo;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;
import com.jeffersonvilla.HabitsTracker.service.implementations.HabitCategoryServiceImpl;

public class HabitCategoryServiceImplTests {
    
    @Mock
    private HabitCategoryRepo habitCategoryRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private HabitCategoryMapper mapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private HabitCategoryServiceImpl habitCategoryService;

    private final String USERNAME = "usernameTest";

    private User user;

    private HabitCategory habitCategoryNoUser;

    private HabitCategoryDto requestDto;

    private HabitCategoryDto responseDto;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        user = new User(1L, USERNAME, "test@email", "password", true);
        habitCategoryNoUser = new HabitCategory(2L, "tesCategoryNoUser", null);
        requestDto = new HabitCategoryDto("testCategory");
        responseDto = new HabitCategoryDto(1L, "testCategory");
    }

    @Test
    public void createHabitCategory_UserNotFound(){

        when(authentication.getName()).thenReturn(USERNAME);

        when(userRepo.findByUsername(USERNAME)).thenReturn(Optional.empty());

        UserNotFoundException exceptionThrown = assertThrows(
            UserNotFoundException.class,    
            () -> {
                habitCategoryService.createHabitCategory(requestDto);
            }
        );

        assertEquals(USER_NOT_FOUND, exceptionThrown.getMessage());

        verify(authentication).getName();
        verify(userRepo).findByUsername(anyString());
        verify(mapper, times(0)).fromDto(any());
        verify(habitCategoryRepo, times(0)).save(any());
        verify(mapper, times(0)).toDto(any());

    } 

    @Test
    public void createHabitCategory_success(){

        when(authentication.getName()).thenReturn(USERNAME);

        when(userRepo.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        when(mapper.fromDto(any())).thenReturn(habitCategoryNoUser);

        when(habitCategoryRepo.save(any())).thenReturn(habitCategoryNoUser);

        when(mapper.toDto(any())).thenReturn(responseDto);

        HabitCategoryDto response = habitCategoryService.createHabitCategory(requestDto);

        assertEquals(responseDto, response);
        assertNotNull(habitCategoryNoUser.getUser()); //User is added before saving

        verify(authentication).getName();
        verify(userRepo).findByUsername(anyString());
        verify(mapper).fromDto(any());
        verify(habitCategoryRepo).save(any());
        verify(mapper).toDto(any());

    } 

    @Test
    public void getAllHabitCategories_UserNotFound(){

        when(authentication.getName()).thenReturn(USERNAME);

        when(userRepo.findByUsername(USERNAME)).thenReturn(Optional.empty());

        UserNotFoundException exceptionThrown = assertThrows(
            UserNotFoundException.class,    
            () -> {
                habitCategoryService.getAllHabitCategories(1L);
            }
        );

        assertEquals(USER_NOT_FOUND, exceptionThrown.getMessage());

        verify(authentication).getName();
        verify(userRepo).findByUsername(anyString());
        verify(habitCategoryRepo, times(0)).findHabitCategoriesByUser(any());
        verify(mapper, times(0)).toDto(any());

    } 

    @Test
    public void getAllHabitCategories_User_NotAuthorized(){

        when(authentication.getName()).thenReturn(USERNAME);

        when(userRepo.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        HabitCategoryAccessDeniedException exceptionThrown = assertThrows(
            HabitCategoryAccessDeniedException.class,    
            () -> {
                habitCategoryService.getAllHabitCategories(2L); //different userId than user object from setUp
            }
        );

        assertEquals(NOT_AUTHORIZED_TO_ACCESS_HABIT_CATEGORIES_OF_USER, exceptionThrown.getMessage());

        verify(authentication).getName();
        verify(userRepo).findByUsername(anyString());
        verify(habitCategoryRepo, times(0)).findHabitCategoriesByUser(any());
        verify(mapper, times(0)).toDto(any());

    } 

    @Test
    public void getAllHabitCategories_success(){

        HabitCategory category1 = mock(HabitCategory.class);
        HabitCategory category2 = mock(HabitCategory.class);
        List<HabitCategory> categoriesFromDB = List.of(category1, category2);

        HabitCategoryDto categoryDto1 = mock(HabitCategoryDto.class);
        HabitCategoryDto categoryDto2 = mock(HabitCategoryDto.class);
        List<HabitCategoryDto> categoriesDtoExpected = List.of(categoryDto1, categoryDto2);

        when(authentication.getName()).thenReturn(USERNAME);

        when(userRepo.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        when(habitCategoryRepo.findHabitCategoriesByUser(anyLong())).thenReturn(categoriesFromDB);

        when(mapper.toDto(category1)).thenReturn(categoryDto1);
        when(mapper.toDto(category2)).thenReturn(categoryDto2);

        List<HabitCategoryDto> responseList = habitCategoryService.getAllHabitCategories(1L);

        assertEquals(categoriesDtoExpected, responseList);

        verify(authentication).getName();
        verify(userRepo).findByUsername(anyString());
        verify(habitCategoryRepo).findHabitCategoriesByUser(any());
        verify(mapper, times(categoriesDtoExpected.size())).toDto(any());

    } 
    
}
