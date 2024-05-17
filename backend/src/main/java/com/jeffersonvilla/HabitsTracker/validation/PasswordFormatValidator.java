package com.jeffersonvilla.HabitsTracker.validation;

import org.springframework.stereotype.Component;

import com.jeffersonvilla.HabitsTracker.exceptions.PasswordFormatException;

@Component
//@Qualifier("passwordFormatValidator")
public class PasswordFormatValidator implements Validator<String>{
    
    private final String PASSWORD_FORMAT = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private final String PASSWORD_FORMAT_EXCEPTION_MESSAGE = "Password must be at least 8 characters "
        +"long, include at least one digit, one uppercase letter, one lowercase letter, one special "
        +"character (@#%$^, etc.), and contain no spaces or tabs.";

    @Override
    public void validate(String password) {

        if(!password.matches(PASSWORD_FORMAT)) throw new PasswordFormatException(PASSWORD_FORMAT_EXCEPTION_MESSAGE);
    }
}
