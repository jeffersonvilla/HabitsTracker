package com.jeffersonvilla.HabitsTracker.service.implementations;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.HABIT_CATEGORY_NOT_FOUND;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.HABIT_NOT_FOUND;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_AUTHORIZED_ACCESS_HABIT;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_AUTHORIZED_ACCESS_HABITS_FOR_USER;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_AUTHORIZED_TO_CREATE_HABIT_FOR_USER;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_AUTORIZED_TO_USE_THIS_CATEGORY;
import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.USER_NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jeffersonvilla.HabitsTracker.Dto.Habit.HabitDto;
import com.jeffersonvilla.HabitsTracker.exceptions.auth.UserNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitAccessDeniedException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryAccessDeniedException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCategoryNotFoundException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitCreationDeniedException;
import com.jeffersonvilla.HabitsTracker.exceptions.habit.HabitNotFoundException;
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
            throw new HabitCreationDeniedException(USER_NOT_AUTHORIZED_TO_CREATE_HABIT_FOR_USER);
        }
        
        Optional<HabitCategory> category = categoryRepo.findById(habitDto.getCategory());
        
        if(category.isEmpty()){
            throw new HabitCategoryNotFoundException(HABIT_CATEGORY_NOT_FOUND);
        }

        if(category.get().getUser()!= null && 
            category.get().getUser().getId() != userOptional.get().getId()){

            throw new HabitCreationDeniedException(USER_NOT_AUTORIZED_TO_USE_THIS_CATEGORY);
        }

        Habit habitToSave = new Habit(habitDto.getName(), habitDto.getDescription(), habitDto.getTrigger(), category.get(), userOptional.get());

        return mapper.toDto(habitRepo.save(habitToSave));
    }

    @Override
    public List<HabitDto> getAllHabits(long userId) {
        
        Optional<User> userOptional = userRepo.findById(userId);
        
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        
        String usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!usernameFromToken.equals(userOptional.get().getUsername())) {
            throw new HabitAccessDeniedException(USER_NOT_AUTHORIZED_ACCESS_HABITS_FOR_USER);
        }

        List<Habit> habitsFound = habitRepo.findByUser(userOptional.get());

        return habitsFound.stream().map((habit)->mapper.toDto(habit)).toList();
    }

    @Override
    public HabitDto getHabit(long habitId) {
        return mapper.toDto(getHabitById(habitId));
    }

    @Override
    public HabitDto updateHabit(long habitId, HabitDto habitDto) {
        
        Habit habitFound = getHabitById(habitId);

        if(habitDto.getName() != null && !habitDto.getName().isBlank()){
            habitFound.setName(habitDto.getName());
        }

        if(habitDto.getDescription() != null){
            habitFound.setDescription(habitDto.getDescription());
        }

        if(habitDto.getTrigger() != null){
            habitFound.setTrigger(habitDto.getTrigger());
        }

        if(habitDto.getCategory() != null){

            Optional<HabitCategory> category = categoryRepo.findById(habitDto.getCategory());
            
            if(category.isEmpty()){
                throw new HabitCategoryNotFoundException(HABIT_CATEGORY_NOT_FOUND);
            }

            if(category.get().getUser()!= null &&
                category.get().getUser().getId() != habitFound.getUser().getId()){
                    
                throw new HabitCategoryAccessDeniedException(USER_NOT_AUTORIZED_TO_USE_THIS_CATEGORY);
            }

            habitFound.setCategory(category.get());

        }

        return mapper.toDto(habitRepo.save(habitFound));

    }

    @Override
    public void deleteHabit(long id) {
        habitRepo.delete(getHabitById(id));
    }
    
    /**
     * Verifies that the habit that is being requested belongs to the user signed in the jwt
     * and returns the habit or throws the corresponding exceptions
     */
    private Habit getHabitById(long habitId){
        
        String usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Optional<User> userOptional = userRepo.findByUsername(usernameFromToken);
        
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        
        Optional<Habit> habitOptional = habitRepo.findById(habitId);

        if(habitOptional.isEmpty()){
            throw new HabitNotFoundException(HABIT_NOT_FOUND);
        }

        if (habitOptional.get().getUser().getId() != userOptional.get().getId()) {
            throw new HabitAccessDeniedException(USER_NOT_AUTHORIZED_ACCESS_HABIT);
        }
        
        return habitOptional.get();
    }
}
