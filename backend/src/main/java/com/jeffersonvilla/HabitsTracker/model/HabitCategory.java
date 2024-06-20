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
@Table(name = "habit_category")
public class HabitCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT")
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public HabitCategory(Long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public HabitCategory() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "HabitCategory [id=" + id + ", name=" + name + ((user != null)? ", user=" + user.toString() : "" )+ "]";
    }
    
}
