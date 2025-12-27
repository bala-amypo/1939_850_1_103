package com.example.demo.repository;

import com.example.demo.model.EmployeeAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<EmployeeAvailability, Long> {
    
    // Checks for existing records to prevent duplicates
    // Standard naming: findBy + EntityName + PropertyName
    Optional<EmployeeAvailability> findByEmployeeIdAndAvailableDate(Long employeeId, LocalDate availableDate);
    
    // Essential for the scheduling engine to find who is actually 'available' on a date
    List<EmployeeAvailability> findByAvailableDateAndAvailable(LocalDate availableDate, boolean available);
    
    // Finds all availability records for a specific employee
    List<EmployeeAvailability> findByEmployeeId(Long employeeId);
}