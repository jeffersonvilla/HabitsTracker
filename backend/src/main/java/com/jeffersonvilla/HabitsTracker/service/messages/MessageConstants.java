package com.jeffersonvilla.HabitsTracker.service.messages;

public class MessageConstants {
    
    public static final String USERNAME_IN_USE_MESSAGE = "Username already exists.";
    public static final String EMAIL_IN_USE_MESSAGE = "Email already exists.";
    public static final String INVALID_VERIFICATION_TOKEN = "Verification token not valid.";
    public static final String PASSWORD_FORMAT_EXCEPTION_MESSAGE = "Password must be at least 8 characters "
        +"long, include at least one digit, one uppercase letter, one lowercase letter, one special "
        +"character (@#%$^, etc.), and contain no spaces or tabs.";

    public static final String NEEDED_USERNAME_OR_EMAIL = "The username (or email) is needed for login.";
    public static final String USER_NOT_FOUND = "User with username or email not found.";
    public static final String INVALID_PASSWORD = "The password is not correct.";
}
