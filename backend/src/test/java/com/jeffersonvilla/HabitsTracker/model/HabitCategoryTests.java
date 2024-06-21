package com.jeffersonvilla.HabitsTracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HabitCategoryTests {
    
    @Test
    public void testConstructorAndGetters() {
        
        // Test constructor
        User user = new User(1L, "john_doe", "john@example.com", "password123", true);
        HabitCategory category = new HabitCategory(1L, "Health", user);

        // Assert getters
        assertEquals(1L, category.getId());
        assertEquals("Health", category.getName());
        assertEquals(user.toString(), category.getUser().toString());
    }

    @Test
    public void testSetters() {
        // Create User instance
        User user = new User(1L, "john_doe", "john@example.com", "password123", true);
        HabitCategory category = new HabitCategory();

        // Set values using setters
        category.setId(2L);
        category.setName("Finances");
        category.setUser(user);
        // Assert changes
        assertEquals(2L, category.getId());
        assertEquals("Finances", category.getName());
        assertEquals(user.toString(), category.getUser().toString());
    }

    @Test
    public void testToString_withUser() {
        // Create User instance
        User user = new User(3L, "mary_jones", "mary@example.com", "pass123", true);
        HabitCategory category = new HabitCategory(3L, "Productivity", user);

        // Test toString method  
        String expectedUseString = "User [id=3, username=mary_jones, email=mary@example.com, verified=true]";
        String expectedCategoryString = "HabitCategory [id=3, name=Productivity, user="+expectedUseString+"]";
        
        assertEquals(expectedCategoryString, category.toString());
    }

    @Test
    public void testToString_userNull() {
        // Create User instance
        HabitCategory category = new HabitCategory(3L, "Productivity", null);

        // Test toString method  
        String expectedCategoryString = "HabitCategory [id=3, name=Productivity]";
        
        assertEquals(expectedCategoryString, category.toString());
    }

}
