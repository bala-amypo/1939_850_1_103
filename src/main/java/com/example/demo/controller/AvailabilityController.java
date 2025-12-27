package com.example.demo.controller;

import com.example.demo.model.EmployeeAvailability;
import com.example.demo.service.AvailabilityService;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final EmployeeRepository employeeRepository;

    public AvailabilityController(AvailabilityService availabilityService,
                                   EmployeeRepository employeeRepository) {
        this.availabilityService = availabilityService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<EmployeeAvailability> create(
            @PathVariable Long employeeId,
            @RequestBody EmployeeAvailability availability) {

        return ResponseEntity.ok(availabilityService.create(availability));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeAvailability>> byEmployee(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(availabilityService.getByEmployee(employeeId));
    }

    @GetMapping("/{availabilityId}")
    public ResponseEntity<EmployeeAvailability> get(
            @PathVariable Long availabilityId) {

        return ResponseEntity.ok(availabilityService.get(availabilityId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeAvailability> update(
            @PathVariable Long id,
            @RequestBody EmployeeAvailability availability) {

        return ResponseEntity.ok(availabilityService.update(id, availability));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        availabilityService.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<EmployeeAvailability>> byDate(
            @PathVariable String date) {

        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(availabilityService.getByDate(localDate));
    }
}
