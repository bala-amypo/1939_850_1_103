package com.example.demo.service.impl;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (employee.getMaxWeeklyHours() == null || employee.getMaxWeeklyHours() <= 0) {
            throw new RuntimeException("Max hours per week must be greater than 0");
        }
        
        if (employee.getRole() == null || employee.getRole().isEmpty()) {
            employee.setRole("STAFF");
        }
        
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if (employee.getEmail() != null && !employee.getEmail().equals(existing.getEmail())) {
            if (employeeRepository.existsByEmail(employee.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            existing.setEmail(employee.getEmail());
        }
        
        if (employee.getFullName() != null) {
            existing.setFullName(employee.getFullName());
        }
        
        if (employee.getRole() != null) {
            existing.setRole(employee.getRole());
        }
        
        if (employee.getSkills() != null) {
            existing.setSkills(employee.getSkills());
        }
        
        if (employee.getMaxWeeklyHours() != null) {
            existing.setMaxWeeklyHours(employee.getMaxWeeklyHours());
        }
        
        if (employee.getPassword() != null) {
            existing.setPassword(employee.getPassword());
        }
        
        return employeeRepository.save(existing);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.delete(employee);
    }

    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }
}

