package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String fullName;
    private String email;
    
    // The test suite expects a Department entity relationship
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department; 
    
    private String role; // Default to "STAFF"
    private String skills; // e.g., "JAVA,SQL"
    private int maxWeeklyHours;

    // Updated constructor to include Department as required by the Test Suite
    public Employee(String fullName, String email, Department department, String role, String skills, int maxWeeklyHours) {
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.role = (role == null) ? "STAFF" : role;
        this.skills = skills;
        this.maxWeeklyHours = maxWeeklyHours;
    }
}