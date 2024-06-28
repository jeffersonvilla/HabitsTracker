package com.jeffersonvilla.HabitsTracker.Dto.Habit;

import jakarta.validation.constraints.NotBlank;

public class HabitCategoryDto {

    private Long categoryId;

    @NotBlank
    private String name;

    public HabitCategoryDto() {
    }

    public HabitCategoryDto(Long categoryId, @NotBlank String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public HabitCategoryDto(@NotBlank String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HabitCategoryDto [categoryId=" + categoryId + ", name=" + name + "]";
    }


    
}
