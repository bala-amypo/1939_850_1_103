package com.example.demo.repository;

import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Critical for login and profile lookups
    Optional<Employee> findByEmail(String email);
    
    // Used by EmployeeService to throw the "exists" exception required by tests
    boolean existsByEmail(String email);

    // FIX: Changed from 'department' to 'role' to match your Employee model fields
    // This prevents the "No property department found" crash.
    List<Employee> findByRole(String role);
}