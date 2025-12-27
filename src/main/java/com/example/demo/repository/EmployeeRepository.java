package com.example.demo.repository;

import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Required for authentication tests
    Optional<Employee> findByEmail(String email);
    
    // Required for registration "exists" validation
    boolean existsByEmail(String email);

    // CRITICAL: The MasterTestNGSuiteTest explicitly calls this.
    // It traverses the Employee -> Department -> Id relationship.
    List<Employee> findByDepartment_Id(Long departmentId);
}