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
    private String role; // Default to "STAFF"
    private String skills; // e.g., "JAVA,SQL"
    private int maxWeeklyHours;

    public Employee(String fullName, String email, String role, String skills, int maxWeeklyHours) {
        this.fullName = fullName;
        this.email = email;
        this.role = (role == null) ? "STAFF" : role;
        this.skills = skills;
        this.maxWeeklyHours = maxWeeklyHours;
    }
}