package com.example.demo.repository;

import com.example.demo.model.EmployeeAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<EmployeeAvailability, Long> {
    
    // Used by Service to check for duplicates before saving
    Optional<EmployeeAvailability> findByEmployee_IdAndAvailableDate(Long employeeId, LocalDate availableDate);
    
    // CRITICAL: Used by ScheduleServiceImpl.generateForDate to find staff for a specific day
    List<EmployeeAvailability> findByAvailableDateAndAvailable(LocalDate availableDate, boolean available);
    
    // Used by AvailabilityController to fetch history for a specific employee
    List<EmployeeAvailability> findByEmployee_Id(Long employeeId);
}