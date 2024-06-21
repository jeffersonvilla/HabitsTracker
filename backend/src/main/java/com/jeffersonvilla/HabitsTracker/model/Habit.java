package com.jeffersonvilla.HabitsTracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "habit")
public class Habit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT")
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "habit_trigger")
    private String trigger;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private HabitCategory category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Habit(Long id, String name, String description, String trigger, HabitCategory category, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.trigger = trigger;
        this.category = category;
        this.user = user;
    }

    public Habit(String name, String description, String trigger, HabitCategory category, User user) {
        this.name = name;
        this.description = description;
        this.trigger = trigger;
        this.category = category;
        this.user = user;
    }

    public Habit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public HabitCategory getCategory() {
        return category;
    }

    public void setCategory(HabitCategory category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Habit [id=" + id + ", name=" + name + ", description=" + description + ", trigger=" + trigger
                + ", category=" + category.toString() + ", user=" + user.toString() + "]";
    }

    

    
}
