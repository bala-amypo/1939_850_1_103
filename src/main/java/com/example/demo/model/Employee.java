package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor // This generates the (id, fullName, email, department, role, skills, maxWeeklyHours) constructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String fullName;
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department; 
    
    private String role; 
    private String skills; 
    private int maxWeeklyHours;

    // KEEP THIS ONE: Used for creating new employees where the ID is not yet known
    public Employee(String fullName, String email, Department department, String role, String skills, int maxWeeklyHours) {
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.role = (role == null) ? "STAFF" : role;
        this.skills = skills;
        this.maxWeeklyHours = maxWeeklyHours;
    }
    
    // REMOVED: The manual constructor with 'id' as it conflicted with @AllArgsConstructor
}