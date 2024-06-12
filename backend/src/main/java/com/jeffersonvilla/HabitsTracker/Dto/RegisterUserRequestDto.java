package com.jeffersonvilla.HabitsTracker.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterUserRequestDto {
    
    @NotBlank
    @NotEmpty
    @Size(min = 4, max = 20)
    private String username;
    
    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    public RegisterUserRequestDto() {
    }

    public RegisterUserRequestDto(@NotBlank @NotEmpty @Size(min = 4, max = 20) String username, @Email @NotEmpty String email,
            @NotEmpty String password) {
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

    @Override
    public String toString() {
        return "RegisterUserRequestDto [username=" + username + ", email=" + email + "]";
    }
    
}
