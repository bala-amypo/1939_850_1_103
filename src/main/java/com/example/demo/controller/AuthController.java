package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.config.JwtUtil; // Matches MasterTestNGSuiteTest import
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoints")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil; // Renamed for test compatibility
    private final PasswordEncoder passwordEncoder;

    // Strict Requirement: Constructor Injection
    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<User> register(@RequestBody User user) {
        // Business logic must reside in service (userService.register handles "exists" check)
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            User user = userService.findByEmail(email);
            
            // Validate password and generate token using renamed utility
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(user.getEmail()); // Method name should match your JwtUtil
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            // SRS requirement: Don't expose internal details
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}