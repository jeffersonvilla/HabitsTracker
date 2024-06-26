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

    public static final String JWT_TOKEN_EXPIRED = "The jwt token has expired";
    public static final String JWT_TOKEN_NOT_VALID = "The jwt token is not valid";

    public static final String HABIT_CATEGORY_NOT_FOUND = "Habit category not found for the id";
    public static final String USER_NOT_AUTHORIZED_TO_CREATE_HABIT_FOR_USER = "You are not authorized to create a habit for this user.";
    public static final String USER_NOT_AUTORIZED_TO_USE_THIS_CATEGORY = "You are not authorized to use this category.";
    public static final String USER_NOT_AUTHORIZED_ACCESS_HABITS_FOR_USER = "You are not authorized to see the list of habits of this user.";
    public static final String HABIT_NOT_FOUND = "Habit not found for the id";
    public static final String USER_NOT_AUTHORIZED_ACCESS_HABIT = "You are not authorized to see this habit.";

    public static final String HABIT_DELETED_SUCCESS = "Habit deleted successfully";

    public static final String NOT_AUTHORIZED_TO_ACCESS_HABIT_CATEGORIES_OF_USER = "You are not authorized to see the list of habit categories of this user.";
    public static final String NOT_AUTHORIZED_TO_ACCESS_HABIT_CATEGORIY = "You are not authorized to see this habit category.";
    public static final String CANT_DELETE_HABIT_CATEGORY_IN_USE = "This habit category is in use, you cant delete it";
}
