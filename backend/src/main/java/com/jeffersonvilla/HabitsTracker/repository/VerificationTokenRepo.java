package com.jeffersonvilla.HabitsTracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeffersonvilla.HabitsTracker.model.VerificationToken;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long>{
    
    public boolean existsByToken(String token);
    public Optional<VerificationToken> findByToken(String token);
}
