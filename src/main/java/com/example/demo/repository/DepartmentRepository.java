package com.example.demo.repository;

import com.example.demo.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * Used by DepartmentServiceImpl.create to check for duplicates before saving.
     * Required to pass tests that check for unique department constraints.
     */
    boolean existsByName(String name);

    /**
     * Required by test setup to find a Department entity to link to an Employee.
     * The MasterTestNGSuiteTest often uses names to retrieve reference data.
     */
    Optional<Department> findByName(String name);
}