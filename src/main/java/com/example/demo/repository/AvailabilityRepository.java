package com.example.demo.repository;

import com.example.demo.model.EmployeeAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<EmployeeAvailability, Long> {
    
    // FIX 1: Add the underscore to match Service line 22
    Optional<EmployeeAvailability> findByEmployee_IdAndAvailableDate(Long employeeId, LocalDate availableDate);
    
    // FIX 2: Add the underscore to match Service line 33
    List<EmployeeAvailability> findByEmployee_Id(Long employeeId);
    
    // Keep this for the scheduling logic
    List<EmployeeAvailability> findByAvailableDateAndAvailable(LocalDate availableDate, boolean available);
}