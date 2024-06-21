package com.jeffersonvilla.HabitsTracker.Dto.Habit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HabitDto {
    
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private String trigger;

    @NotNull
    private Long category;

    @NotNull
    private Long user;

    public HabitDto() {
    }

    public HabitDto(Long id, @NotBlank String name, String description, String trigger, @NotNull Long category,
            @NotNull Long user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.trigger = trigger;
        this.category = category;
        this.user = user;
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

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "HabitDto [id=" + id + ", name=" + name + ", description=" + description + ", trigger=" + trigger
                + ", category=" + category + ", user=" + user + "]";
    }

}
