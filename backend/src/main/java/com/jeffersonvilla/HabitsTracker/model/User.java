package com.jeffersonvilla.HabitsTracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUser", columnDefinition = "INT")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;   

    @Column(name = "password")
    private String password;
}
