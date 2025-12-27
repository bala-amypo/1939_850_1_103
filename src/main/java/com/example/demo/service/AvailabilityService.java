package com.example.demo.service;

import com.example.demo.model.EmployeeAvailability;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    EmployeeAvailability saveAvailability(EmployeeAvailability availability);
    List<EmployeeAvailability> getByEmployee(Long empId);
    List<EmployeeAvailability> getByDate(LocalDate date);
}