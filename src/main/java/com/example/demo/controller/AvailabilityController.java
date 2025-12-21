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

    public AvailabilityController(AvailabilityService availabilityService, EmployeeRepository employeeRepository) {
        this.availabilityService = availabilityService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/{employeeId}")
    @Operation(summary = "Create availability")
    public ResponseEntity<EmployeeAvailability> create(@PathVariable Long employeeId, @RequestBody AvailabilityDto dto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        EmployeeAvailability availability = new EmployeeAvailability(employee, dto.getAvailableDate(), dto.getAvailable());
        return ResponseEntity.ok(availabilityService.create(availability));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get availability by employee")
    public ResponseEntity<List<EmployeeAvailability>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeRepository.findById(employeeId)
                .map(emp -> availabilityService.getByDate(LocalDate.now()))
                .orElseThrow(() -> new RuntimeException("Employee not found")));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get availability by date")
    public ResponseEntity<List<EmployeeAvailability>> getByDate(@PathVariable String date) {
        return ResponseEntity.ok(availabilityService.getByDate(LocalDate.parse(date)));
    }
}