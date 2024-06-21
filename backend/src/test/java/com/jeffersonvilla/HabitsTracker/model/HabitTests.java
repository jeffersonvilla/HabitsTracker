package com.jeffersonvilla.HabitsTracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HabitTests {
    @Test
    public void testConstructorAndGetters() {
        
        // Test constructor
        User user = new User(1L, "john_doe", "john@example.com", "password123", true);
        HabitCategory category = new HabitCategory(1L, "Health", user);
        Habit habit = new Habit(1L, "Wake up early", "Wake up before 9:00 a.m.", "Alarm", category, user);

        // Assert getters
        assertEquals(1L, habit.getId());
        assertEquals("Wake up early", habit.getName());
        assertEquals("Wake up before 9:00 a.m.", habit.getDescription());
        assertEquals("Alarm", habit.getTrigger());
        assertEquals(category.toString(), habit.getCategory().toString());
        assertEquals(user.toString(), habit.getUser().toString());
    }

    @Test
    public void testSetters() {
        String name = "Excercise";
        String description = "Go to the gym in the afternoon";
        String trigger = "Time of the day";
        User user = new User(1L, "john_doe", "john@example.com", "password123", true);
        HabitCategory category = new HabitCategory();
        
        // Create User instance
        Habit habit = new Habit();

        // Set values using setters
        habit.setId(2L);
        habit.setName(name);
        habit.setDescription(description);
        habit.setTrigger(trigger);
        habit.setCategory(category);
        habit.setUser(user);

        // Assert changes
        assertEquals(2L, habit.getId());
        assertEquals(name, habit.getName());
        assertEquals(description, habit.getDescription());
        assertEquals(trigger, habit.getTrigger());
        assertEquals(category.toString(), habit.getCategory().toString());
        assertEquals(user.toString(), habit.getUser().toString());
    }

    @Test
    public void testToString() {
        String name = "Excercise";
        String description = "Go to the gym in the afternoon";
        String trigger = "Time of the day";
        
        User user = new User(3L, "mary_jones", "mary@example.com", "pass123", true);
        HabitCategory category = new HabitCategory(3L, "Productivity", user);

        // Create User instance
        Habit habit = new Habit(3L, name, description, trigger, category, user);

        // Test toString method  
        String expectedHabitString = "Habit [id=3, name=" + name + ", description=" + description + ", trigger=" + trigger+ ", category=" + category.toString() + ", user=" + user.toString() + "]";    

        assertEquals(expectedHabitString, habit.toString());
    }
}
