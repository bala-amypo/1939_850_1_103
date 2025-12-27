package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String password;
    private String role; // e.g., "ROLE_ADMIN", "ROLE_USER"

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}