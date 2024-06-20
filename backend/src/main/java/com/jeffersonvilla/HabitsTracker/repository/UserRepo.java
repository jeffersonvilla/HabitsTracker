package com.jeffersonvilla.HabitsTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeffersonvilla.HabitsTracker.model.User;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);

    //public User findByUsername(String username);
    //public User findByEmail(String email);

    public Optional<User> findByUsernameOrEmail(String username, String email);
}
