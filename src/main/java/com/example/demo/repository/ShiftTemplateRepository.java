package com.example.demo.repository;

import com.example.demo.model.ShiftTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftTemplateRepository extends JpaRepository<ShiftTemplate, Long> {
    
    // Required for uniqueness validation: "Template name must be unique within department"
    Optional<ShiftTemplate> findByTemplateNameAndDepartment_Id(String templateName, Long departmentId);
    
    // Required for retrieving all requirements for a specific department
    List<ShiftTemplate> findByDepartment_Id(Long departmentId);

    // Optimization: Find templates based on a specific skill (useful for matching algorithm)
    List<ShiftTemplate> findByRequiredSkillsContaining(String skill);
}