package com.jeffersonvilla.HabitsTracker.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeffersonvilla.HabitsTracker.Dto.LoginRequestDto;

@RestController
@RequestMapping("api/v1/test")
public class TestSecurityController {

    @PostMapping("hello")
    public String hello(@RequestBody LoginRequestDto dto){
        return "hello" + dto.getUsername();
    }
    
}
