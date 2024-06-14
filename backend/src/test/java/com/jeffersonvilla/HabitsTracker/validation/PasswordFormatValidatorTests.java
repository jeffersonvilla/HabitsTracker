package com.jeffersonvilla.HabitsTracker.validation;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.PASSWORD_FORMAT_EXCEPTION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.jeffersonvilla.HabitsTracker.exceptions.PasswordFormatException;

public class PasswordFormatValidatorTests {

    @InjectMocks
    private PasswordFormatValidator passwordFormatValidator;
    
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_ValidPasswords() {
        // Passwords that meet the format requirements
        String[] validPasswords = {
            "Aa1@abcd",
            "P@ssw0rd123",
            "1A@bcdefg",
            "Valid1@Password"
        };

        for (String password : validPasswords) {
            assertDoesNotThrow(() -> passwordFormatValidator.validate(password));
        }
    }

    @Test
    public void test_InvalidPasswords() {
        // Passwords that do not meet the format requirements
        String[] invalidPasswords = {
            "abc",                    // Too short
            "abcdefgh",               // No digits, uppercase, or special characters
            "ABCDEFGH",               // No digits, lowercase, or special characters
            "12345678",               // No letters or special characters
            "Abcdefgh",               // No digits or special characters
            "A1@ABCDEFG",             // No lowercase
            "Aa1abcd",                // No special characters
            "Aa1@ bcdef",             // Contains whitespace
        };

        for (String password : invalidPasswords) {
            System.out.println("pass: "+ password);
            PasswordFormatException ex = assertThrows(
                PasswordFormatException.class, 
                () -> {
                    passwordFormatValidator.validate(password);
                }
            );
            assertEquals(PASSWORD_FORMAT_EXCEPTION_MESSAGE, ex.getMessage());
        }
    }
}
