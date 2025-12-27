package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id") // Explicitly links to the Employee's ID
    private Employee employee;

    private LocalDate availableDate;
    
    // Using Boolean (wrapper) is safer than boolean (primitive) 
    // for JPA to handle potential nulls during record creation
    private Boolean available;

    public EmployeeAvailability(Employee employee, LocalDate availableDate, Boolean available) {
        this.employee = employee;
        this.availableDate = availableDate;
        this.available = available;
    }
}