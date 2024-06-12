package com.jeffersonvilla.HabitsTracker.Dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {

    public String username;

    public String email;

    @NotBlank
    public String password;

    public LoginRequestDto() {
    }

    public LoginRequestDto(String username, String email, @NotBlank String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
}
