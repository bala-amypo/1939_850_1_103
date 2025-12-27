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
    
    // FIX: Added password field so Lombok can generate getPassword()
    private String password; 
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department; 
    
    private String role; 
    private String skills; 
    private int maxWeeklyHours;

    // Updated constructor to include password field
    public Employee(String fullName, String email, String password, Department department, String role, String skills, int maxWeeklyHours) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.role = (role == null) ? "STAFF" : role;
        this.skills = skills;
        this.maxWeeklyHours = maxWeeklyHours;
    }
}