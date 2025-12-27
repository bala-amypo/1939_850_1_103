package com.example.demo.service;

import com.example.demo.model.EmployeeAvailability;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    
    // Create
    EmployeeAvailability saveAvailability(EmployeeAvailability availability);
    
    // Read
    List<EmployeeAvailability> getByEmployee(Long empId);
    
    List<EmployeeAvailability> getByDate(LocalDate date);
    
    // Update - FIX: Required for the updateAvailability logic in your Impl
    EmployeeAvailability update(Long id, EmployeeAvailability availability);
    
    // Delete - FIX: Required for the deleteAvailability logic in your Impl
    void delete(Long id);
}