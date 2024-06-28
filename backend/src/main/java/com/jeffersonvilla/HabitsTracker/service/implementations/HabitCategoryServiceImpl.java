package com.jeffersonvilla.HabitsTracker.service.implementations;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.CANT_DELETE_HABIT_CATEGORY_IN_USE;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.HABIT_CATEGORY_NOT_FOUND;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.NOT_AUTHORIZED_TO_ACCESS_HABIT_CATEGORIES_OF_USER;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.NOT_AUTHORIZED_TO_ACCESS_HABIT_CATEGORIY;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_AUTORIZED_TO_USE_THIS_CATEGORY;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitCategoryDto;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryAccessDeniedException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryDeniedDeleteException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryNotFoundException;
import com.jeffersonvilla.HabitsTracker.mapper.Mapper;
import com.jeffersonvilla.HabitsTracker.model.HabitCategory;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.repository.HabitCategoryRepo;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;
import com.jeffersonvilla.HabitsTracker.service.interfaces.HabitCategoryService;

@Service
public class HabitCategoryServiceImpl implements HabitCategoryService{

    private final HabitCategoryRepo habitCategoryRepo;
    private final UserRepo userRepo;
    private final Mapper<HabitCategory, HabitCategoryDto> mapper;

    public HabitCategoryServiceImpl(HabitCategoryRepo habitCategoryRepo, UserRepo userRepo,
        Mapper<HabitCategory, HabitCategoryDto> mapper){

        this.habitCategoryRepo = habitCategoryRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
    }

    @Override
    public HabitCategoryDto createHabitCategory(HabitCategoryDto dto) {
        
        Optional<User> userOptional = getUserFromJWT();
        
        HabitCategory category = mapper.fromDto(dto);
        category.setUser(userOptional.get());

        return mapper.toDto(habitCategoryRepo.save(category));

    }

    @Override
    public List<HabitCategoryDto> getAllHabitCategories(Long userId) {

        Optional<User> userOptional = getUserFromJWT();

        if(userOptional.get().getId() != userId){
            throw new HabitCategoryAccessDeniedException(NOT_AUTHORIZED_TO_ACCESS_HABIT_CATEGORIES_OF_USER);
        }
        
        return habitCategoryRepo.findHabitCategoriesByUser(userId)
            .stream().map((HabitCategory category) -> mapper.toDto(category)).toList();
    }

    @Override
    public HabitCategoryDto getHabitCategory(Long categoryId) {
        
        Optional<User> userOptional = getUserFromJWT();

        Optional<HabitCategory> categoryFound = habitCategoryRepo.findById(categoryId);

        if(categoryFound.isEmpty()){
            throw new HabitCategoryNotFoundException(HABIT_CATEGORY_NOT_FOUND);
        }

        if(categoryFound.get().getUser() != null 
            && categoryFound.get().getUser().getId() != userOptional.get().getId()){

            throw new HabitCategoryAccessDeniedException(NOT_AUTHORIZED_TO_ACCESS_HABIT_CATEGORIY);
        }

        return mapper.toDto(categoryFound.get());
    }

    @Override
    public HabitCategoryDto updateHabitCategory(Long categoryId, HabitCategoryDto dto) {
        
        Optional<HabitCategory> categoryFound = habitCategoryRepo.findById(categoryId);

        if(categoryFound.isEmpty()){
            throw new HabitCategoryNotFoundException(HABIT_CATEGORY_NOT_FOUND);
        }

        Optional<User> userFromJWT = getUserFromJWT();

        if(categoryFound.get().getUser() == null 
            || categoryFound.get().getUser().getId() != userFromJWT.get().getId()){

            throw new HabitCategoryAccessDeniedException(USER_NOT_AUTORIZED_TO_USE_THIS_CATEGORY);
        }

        HabitCategory categoryToUpdate = categoryFound.get();

        if(dto.getName() != null){
            categoryToUpdate.setName(dto.getName());
        }

        return mapper.toDto(habitCategoryRepo.save(categoryToUpdate));
    }

    @Override
    public void deleteHabitCategory(Long categoryId) {

        Optional<HabitCategory> categoryFound = habitCategoryRepo.findById(categoryId);

        if(categoryFound.isEmpty()){
            throw new HabitCategoryNotFoundException(HABIT_CATEGORY_NOT_FOUND);
        }

        Optional<User> userFromJWT = getUserFromJWT();

        if(categoryFound.get().getUser() == null 
            || categoryFound.get().getUser().getId() != userFromJWT.get().getId()){

            throw new HabitCategoryAccessDeniedException(USER_NOT_AUTORIZED_TO_USE_THIS_CATEGORY);
        }

        try{
            habitCategoryRepo.delete(categoryFound.get());
        } catch(DataIntegrityViolationException ex){
            throw new HabitCategoryDeniedDeleteException(CANT_DELETE_HABIT_CATEGORY_IN_USE);
        }

    }

    private Optional<User> getUserFromJWT(){

        String usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> userOptional = userRepo.findByUsername(usernameFromToken);
        
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        return userOptional;
    }
    
}
