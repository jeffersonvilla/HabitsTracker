package com.jeffersonvilla.HabitsTracker.service.implementations;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.HABIT_CATEGORY_NOT_FOUND;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_AUTORIZED_TO_CREATE_HABIT_FOR_USER;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitDto;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCreationDeniedException;
import com.jeffersonvilla.HabitsTracker.mapper.Mapper;
import com.jeffersonvilla.HabitsTracker.model.Habit;
import com.jeffersonvilla.HabitsTracker.model.HabitCategory;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.repository.HabitCategoryRepo;
import com.jeffersonvilla.HabitsTracker.repository.HabitRepo;
import com.jeffersonvilla.HabitsTracker.repository.UserRepo;
import com.jeffersonvilla.HabitsTracker.service.interfaces.HabitService;

@Service
public class HabitServiceImpl implements HabitService{

    private final HabitRepo habitRepo;
    private final HabitCategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final Mapper<Habit, HabitDto> mapper;


    public HabitServiceImpl(HabitRepo habitRepo, HabitCategoryRepo categoryRepo, UserRepo userRepo
        , Mapper<Habit, HabitDto> mapper){
        this.habitRepo = habitRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
    }

    @Override
    public HabitDto createHabit(HabitDto habitDto) {

        Optional<User> userOptional = userRepo.findById(habitDto.getUser());
        
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        
        String usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!usernameFromToken.equals(userOptional.get().getUsername())) {
            throw new HabitCreationDeniedException(USER_NOT_AUTORIZED_TO_CREATE_HABIT_FOR_USER);
        }
        
        Optional<HabitCategory> category = categoryRepo.findById(habitDto.getCategory());
        
        if(category.isEmpty()){
            throw new HabitCategoryNotFoundException(HABIT_CATEGORY_NOT_FOUND);
        }

        Habit habitToSave = new Habit(habitDto.getName(), habitDto.getDescription(), habitDto.getTrigger(), category.get(), userOptional.get());

        return mapper.toDto(habitRepo.save(habitToSave));
    }

    @Override
    public List<HabitDto> getAllHabits(long userOptional) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllHabits'");
    }

    @Override
    public HabitDto getHabit(long habitId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHabit'");
    }

    @Override
    public HabitDto updateHabit(long habitId, HabitDto habitDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateHabit'");
    }

    @Override
    public void deletehabit(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletehabit'");
    }
    
}
