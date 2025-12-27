package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, 
                          EmployeeRepository employeeRepository, 
                          PasswordEncoder passwordEncoder, 
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Employee employee) {
        // Hash password before saving
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        if (employee.getRole() == null) employee.setRole("STAFF");
        
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        // 1. Validate credentials (this throws 401 if wrong)
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), 
                authRequest.getPassword()
            )
        );

        // 2. Fetch user details for the response
        Employee employee = employeeRepository.findByEmail(authRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Generate Token
        String token = jwtUtil.generateToken(employee.getEmail());

        // 4. Return the DTO you created
        return ResponseEntity.ok(new AuthResponse(
            token,
            employee.getId(),
            employee.getEmail(),
            employee.getRole()
        ));
    }
}