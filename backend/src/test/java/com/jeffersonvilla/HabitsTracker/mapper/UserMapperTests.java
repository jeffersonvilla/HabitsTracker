package com.jeffersonvilla.HabitsTracker.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.jeffersonvilla.HabitsTracker.Dto.RegisterUserRequestDto;
import com.jeffersonvilla.HabitsTracker.model.User;

public class UserMapperTests {

    @InjectMocks
    private UserMapper userMapper;

    private User model;
    
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        model = new User(1L, "testUsername", "test@email","password",true);
    }

    @Test
    void test_toDto(){

        RegisterUserRequestDto dto = new RegisterUserRequestDto("testUsername", "test@email", null);

        RegisterUserRequestDto resultDto = userMapper.toDto(model);

        assertEquals(dto.getUsername(), resultDto.getUsername());
        assertEquals(dto.getEmail(), resultDto.getEmail());
        assertEquals(dto.getPassword(), resultDto.getPassword());
    }

    @Test
    void test_fromDto(){

        RegisterUserRequestDto dto = new RegisterUserRequestDto("testUsername", "test@email", "password");

        User resultUser = userMapper.fromDto(dto);

        assertEquals(model.getUsername(), resultUser.getUsername());
        assertEquals(model.getEmail(), resultUser.getEmail());
        assertEquals(model.getPassword(), resultUser.getPassword());
    }
}
