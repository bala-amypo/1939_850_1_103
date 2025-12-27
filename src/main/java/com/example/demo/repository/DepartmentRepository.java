package com.example.demo.repository;

import com.example.demo.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    // Used by DepartmentServiceImpl.create to check for duplicates
    boolean existsByName(String name);

    // Often required by test setup to link employees to specific departments by name
    Optional<Department> findByName(String name);
}