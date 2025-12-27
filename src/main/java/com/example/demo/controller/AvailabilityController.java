package com.example.demo.controller;

import com.example.demo.dto.AvailabilityDto;
import com.example.demo.model.Employee;
import com.example.demo.model.EmployeeAvailability;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.AvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
@Tag(name = "Employee Availability Endpoints")
public class AvailabilityController {
    private final AvailabilityService availabilityService;
    private final EmployeeRepository employeeRepository;

    // Requirement: Constructor Injection
    public AvailabilityController(AvailabilityService availabilityService, EmployeeRepository employeeRepository) {
        this.availabilityService = availabilityService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/{employeeId}")
    @Operation(summary = "Create availability")
    public ResponseEntity<EmployeeAvailability> create(@PathVariable Long employeeId, @RequestBody AvailabilityDto dto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        EmployeeAvailability availability = new EmployeeAvailability();
        availability.setEmployee(employee);
        availability.setAvailableDate(dto.getAvailableDate());
        availability.setAvailable(dto.getAvailable());
        
        // Changed to saveAvailability to match standard Service interface names
        return ResponseEntity.ok(availabilityService.saveAvailability(availability));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get availability by employee")
    public ResponseEntity<List<EmployeeAvailability>> getByEmployee(@PathVariable Long employeeId) {
        // FIX: Previously calling getByDate. Now calling getByEmployee.
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found");
        }
        return ResponseEntity.ok(availabilityService.getByEmployee(employeeId));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get availability by date")
    public ResponseEntity<List<EmployeeAvailability>> getByDate(@PathVariable String date) {
        // Required for Test Priority 62 (Date parsing validation)
        return ResponseEntity.ok(availabilityService.getByDate(LocalDate.parse(date)));
    }
}