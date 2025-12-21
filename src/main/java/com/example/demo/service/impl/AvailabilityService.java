package com.example.demo.service;

import com.example.demo.model.EmployeeAvailability;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    EmployeeAvailability create(EmployeeAvailability availability);
    EmployeeAvailability update(Long id, EmployeeAvailability availability);
    void delete(Long id);
    List<EmployeeAvailability> getByDate(LocalDate date);
}