package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String templateName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String requiredSkills;

    @ManyToOne
    private Department department;

    public ShiftTemplate(String templateName, LocalTime startTime, LocalTime endTime, String requiredSkills, Department department) {
        this.templateName = templateName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.requiredSkills = requiredSkills;
        this.department = department;
    }
}