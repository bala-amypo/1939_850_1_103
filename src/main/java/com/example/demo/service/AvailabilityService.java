package com.example.demo.service;

import com.example.demo.model.EmployeeAvailability;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    
    // FIX: Added 'create' to match the method in AvailabilityServiceImpl
    EmployeeAvailability create(EmployeeAvailability availability);
    
    // Create
    EmployeeAvailability saveAvailability(EmployeeAvailability availability);
    
    // Read
    List<EmployeeAvailability> getByEmployee(Long empId);
    
    List<EmployeeAvailability> getByDate(LocalDate date);
    
    // Update
    EmployeeAvailability update(Long id, EmployeeAvailability availability);
    
    // Delete
    void delete(Long id);
}