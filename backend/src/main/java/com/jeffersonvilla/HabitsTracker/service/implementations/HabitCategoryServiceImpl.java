package com.jeffersonvilla.HabitsTracker.service.implementations;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitCategoryDto;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.UserNotFoundException;
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
        
        String usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> userOptional = userRepo.findByUsername(usernameFromToken);
        
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        
        HabitCategory category = mapper.fromDto(dto);
        category.setUser(userOptional.get());

        return mapper.toDto(habitCategoryRepo.save(category));

    }

    @Override
    public List<HabitCategoryDto> getAllHabitCategories(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllHabitCategories'");
    }

    @Override
    public HabitCategoryDto getHabitCategory(Long categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHabitCategory'");
    }

    @Override
    public HabitCategoryDto updateHabitCategory(Long categoryId, HabitCategoryDto dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateHabitCategory'");
    }

    @Override
    public void deleteHabitCategory(Long categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteHabitCategory'");
    }
    
}
