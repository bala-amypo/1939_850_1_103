package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // CRITICAL: Used by JwtUtil and CustomUserDetailsService to load the principal
    Optional<User> findByEmail(String email);
    
    // CRITICAL: Used by AuthService to validate registration (Priority 1-5 in TestNG)
    boolean existsByEmail(String email);
}