  SERVICE IMPLEMENTATION FOLDER


AuthServiceImpl.java:
package com.example.demo.service.impl;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(EmployeeRepository employeeRepository, 
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(employee.getEmail());
        return new AuthResponse(token, employee.getEmail(), employee.getRole());
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Employee employee = new Employee();
        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole(request.getRole() != null ? request.getRole() : "STAFF");
        employee.setSkills(request.getSkills());
        employee.setMaxHoursPerWeek(request.getMaxHoursPerWeek());

        employee = employeeRepository.save(employee);

        String token = jwtUtil.generateToken(employee.getEmail());
        return new AuthResponse(token, employee.getEmail(), employee.getRole());
    }
}

AvailabilityServiceImpl.java:
package com.example.demo.service.impl;

import com.example.demo.model.EmployeeAvailability;
import com.example.demo.repository.AvailabilityRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.AvailabilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final EmployeeRepository employeeRepository;

    public AvailabilityServiceImpl(AvailabilityRepository availabilityRepository,
                                  EmployeeRepository employeeRepository) {
        this.availabilityRepository = availabilityRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeAvailability create(EmployeeAvailability availability) {
        if (availability.getEmployee() != null && availability.getEmployee().getId() != null) {
            employeeRepository.findById(availability.getEmployee().getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            
            if (availability.getAvailableDate() != null) {
                Optional<EmployeeAvailability> existing = availabilityRepository.findByEmployee_IdAndAvailableDate(
                        availability.getEmployee().getId(),
                        availability.getAvailableDate()
                );
                
                if (existing.isPresent()) {
                    throw new RuntimeException("Availability already exists for this date");
                }
            }
        }
        
        return availabilityRepository.save(availability);
    }

    @Override
    public EmployeeAvailability update(Long id, EmployeeAvailability availability) {
        EmployeeAvailability existing = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
        
        if (availability.getAvailable() != null) {
            existing.setAvailable(availability.getAvailable());
        }
        
        if (availability.getAvailableDate() != null) {
            existing.setAvailableDate(availability.getAvailableDate());
        }
        
        return availabilityRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        EmployeeAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
        availabilityRepository.delete(availability);
    }

    @Override
    public EmployeeAvailability get(Long id) {
        return availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
    }

    @Override
    public List<EmployeeAvailability> getByDate(LocalDate date) {
        return availabilityRepository.findByAvailableDateAndAvailable(date, true);
    }

    @Override
    public List<EmployeeAvailability> getByEmployee(Long employeeId) {
        return availabilityRepository.findByEmployee_Id(employeeId);
    }
}

DepartmentServiceImpl.java:
package com.example.demo.service.impl;

import com.example.demo.model.Department;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department create(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new IllegalArgumentException("Department name already exists");
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department get(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Override
    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        departmentRepository.delete(department);
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }
}

EmployeeServiceImpl.java:
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

